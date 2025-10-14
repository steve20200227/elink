/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.snowsoft.iot.module.message.service.impl;

import cn.hutool.json.JSONUtil;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.message.cache.manager.ServerCacheManager;
import cn.snowsoft.iot.module.message.constant.TraceIdConstant;
import cn.snowsoft.iot.module.message.enums.StatusEnum;
import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.model.dto.common.PushWayDto;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.model.dto.request.SendRequestDto;
import cn.snowsoft.iot.module.message.model.entity.App;
import cn.snowsoft.iot.module.message.model.entity.Template;
import cn.snowsoft.iot.module.message.model.entity.TemplateApp;
import cn.snowsoft.iot.module.message.pretreatment.common.LinkContext;
import cn.snowsoft.iot.module.message.pretreatment.common.LinkHandler;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import cn.snowsoft.iot.module.message.service.SendService;
import cn.snowsoft.iot.module.message.service.TemplateService;
import cn.snowsoft.iot.module.message.util.*;
import cn.snowsoft.iot.module.system.api.notify.NotifyMessageSendApi;
import cn.snowsoft.iot.module.system.api.notify.dto.NotifySendMessageUserReqDTO;
import cn.snowsoft.iot.module.system.api.notify.dto.NotifySendSingleToUserReqDTO;
import cn.snowsoft.iot.module.system.api.user.AdminUserApi;
import cn.snowsoft.iot.module.system.api.user.dto.AdminUserRespDTO;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 发送消息操作 Service 实现
 *
 * @author oszero
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class SendServiceImpl implements SendService {

    private final ServerCacheManager serverCacheManager;
    private final LinkHandler linkHandler;
    private final AesUtils aesUtils;


    private final NotifyMessageSendApi notifyMessageSendApi;
    private final AdminUserApi adminUserApi;
    private final MessageRecordService messageRecordService;

    @Override
    public String send(SendRequestDto sendRequestDto) {

        //站内信暂时单独处理


        // 1.设置 sendTaskDto
        SendTaskDto sendTaskDto = new SendTaskDto();
        ThreadLocalUtils.setSendTaskDto(sendTaskDto);

        sendTaskDto.setWarningRecordId(sendRequestDto.getWarningRecordId());
        sendTaskDto.setWarningActionId(sendRequestDto.getWarningActionId());
        sendTaskDto.setTemplateId(sendRequestDto.getTemplateId());
        sendTaskDto.setParamMap(sendRequestDto.getParamMap());
        sendTaskDto.setTraceId(MdcUtils.get(TraceIdConstant.TRACE_ID));
        sendTaskDto.setClientIp(IpUtils.getClientIp());
        sendTaskDto.setUsers(sendRequestDto.getUsers());
        sendTaskDto.setRetried(0);
        sendTaskDto.setRetry(sendRequestDto.getRetry());

        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "服务端接收到接入方推送消息");

        // 2.通过 templateId 获取 template
        Long templateId = sendRequestDto.getTemplateId();

        Template template = serverCacheManager.getTemplate(templateId);
        if (Objects.isNull(template)) {
            throw new MessageException(sendTaskDto, "传入的模板 ID 非法，请输入正确的 templateId");
        }

        Integer pushRange = template.getPushRange();
        Integer usersType = template.getUsersType();
        PushWayDto pushWayDto = JSONUtil.toBean(template.getPushWays(), PushWayDto.class);
        Integer channelType = pushWayDto.getChannelType();
        String messageType = pushWayDto.getMessageType();

        sendTaskDto.setPushRange(pushRange);
        sendTaskDto.setUsersType(usersType);
        sendTaskDto.setChannelType(channelType);
        sendTaskDto.setMessageType(messageType);


        // 关闭状态直接返回
        if (StatusEnum.OFF.getStatus().equals(template.getTemplateStatus())) {
            throw new MessageException(sendTaskDto, "此模板已禁用，再次使用请启用此模板");
        }



        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "完成消息模板检测");

        // 3.通过 templateId 获取 appId
        TemplateApp templateApp = serverCacheManager.getTemplateApp(templateId);
        if (Objects.isNull(templateApp)) {
            throw new MessageException(sendTaskDto, "未获取到模板所关联的应用，请检查关联的应用是否存在");
        }

        Long appId = templateApp.getAppId();
        sendTaskDto.setAppId(appId);

        // 4.得到 appConfig
        App app = serverCacheManager.getApp(appId);
        if (Objects.isNull(app)) {
            throw new MessageException(sendTaskDto, "未获取到模板所关联的应用，请检查关联的应用是否存在");
        }
        // 关闭状态直接返回
        if (StatusEnum.OFF.getStatus().equals(app.getAppStatus())) {
            throw new MessageException(sendTaskDto, "模板关联的应用为禁用状态，再次使用请启用");
        }

        sendTaskDto.setAppConfig(aesUtils.decrypt(app.getAppConfig()));

        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "完成消息模板关联应用检测");

        if (channelType==0){
            String s = sendSystemMessage(sendTaskDto);
            return sendTaskDto.getTraceId();
        }

        // 5.处理相关责任链
        LinkContext<SendTaskDto> context = LinkContext.<SendTaskDto>builder()
                .processModel(sendTaskDto)
                .code(usersType + "-" + channelType).build();
        linkHandler.process(context);

        // 6.返回 TraceId
        return sendTaskDto.getTraceId();
    }

    //站内信单独处理
    private String sendSystemMessage(SendTaskDto sendTaskDto) {
        String user = null;
        try {
            List<String> users = sendTaskDto.getUsers();
            user = users.get(0);
            CommonResult<AdminUserRespDTO> user1 = adminUserApi.getUser(Long.parseLong(user));
            if (user1.isSuccess()){
                sendTaskDto.setNickName(user1.getData().getNickname());
            }
            Map<String, Object> paramMap = sendTaskDto.getParamMap();
            String content = paramMap.get("templateContext").toString();
            Map<String, Object> params =  JSONObject.parseObject(paramMap.get("templateParam").toString()).getInnerMap();

            sendTaskDto.setTraceId(UUID.randomUUID().toString());
            NotifySendMessageUserReqDTO notifySendMessageUserReqDTO = new NotifySendMessageUserReqDTO();
            notifySendMessageUserReqDTO.setUserId(Long.parseLong(user));
            notifySendMessageUserReqDTO.setTemplateId(sendTaskDto.getTemplateId());

            notifySendMessageUserReqDTO.setTemplateContent(content);
            notifySendMessageUserReqDTO.setTemplateParams(params);

            CommonResult<Long> longCommonResult = notifyMessageSendApi.sendMessageUser(notifySendMessageUserReqDTO);
            if (longCommonResult.getCode()==0){
                //成功
                messageRecordService.saveMessageRecord(
                        sendTaskDto,
                        StatusEnum.ON,
                        user
                );
            }else {
                sendTaskDto.setFaliReason(longCommonResult.getMsg());
                messageRecordService.saveMessageRecord(
                        sendTaskDto,
                        StatusEnum.OFF,
                        user
                );
            }
        } catch (Exception e) {
            sendTaskDto.setFaliReason(e.getMessage());
            messageRecordService.saveMessageRecord(
                    sendTaskDto,
                    StatusEnum.OFF,
                    user
            );
            throw new MessageException(sendTaskDto, e.getMessage());
        }



        return sendTaskDto.getTraceId();
    }
}
