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

package cn.snowsoft.iot.module.message.handler.impl;

import cn.hutool.json.JSONUtil;
import cn.snowsoft.iot.module.message.cache.manager.ServerCacheManager;
import cn.snowsoft.iot.module.message.client.feishu.FeiShuClient;
import cn.snowsoft.iot.module.message.handler.BaseHandler;
import cn.snowsoft.iot.module.message.model.app.FeiShuApp;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 飞书消费者处理器
 *
 * @author oszero
 * @version 1.0.0
 */
@Component
public class FeiShuHandler extends BaseHandler {

    private final FeiShuClient feiShuClient;
    private final ServerCacheManager serverCacheManager;

    /**
     * 可以批量发送的消息类型
     */
    private static final Set<String> BATCH_MESSAGE_TYPE =
            new HashSet<>(Arrays.asList("text", "image", "post", "share_chat", "interactive"));

    public FeiShuHandler(FeiShuClient feiShuClient, ServerCacheManager serverCacheManager, MessageRecordService messageRecordService) {
        this.feiShuClient = feiShuClient;
        this.serverCacheManager = serverCacheManager;
        this.messageRecordService = messageRecordService;
    }

    @Override
    protected void handle(SendTaskDto sendTaskDto) {
        String appConfigJson = sendTaskDto.getAppConfig();
        FeiShuApp feiShuApp = JSONUtil.toBean(appConfigJson, FeiShuApp.class);

        String tenantAccessToken = serverCacheManager.getFeiShuToken(feiShuApp, sendTaskDto);
        Map<String, Object> paramMap = sendTaskDto.getParamMap();
        String feiShuUserIdType = paramMap.get("feiShuUserIdType").toString();
        String msgType = paramMap.get("msg_type").toString();

        // 移除掉用户判断的 feiShuUserIdType
        paramMap.remove("feiShuUserIdType");

        // 支持发送多种飞书的 usersId，包括 [用户(user_id),用户(email),群组(chat_id),部门(department_id)]
        if ("user_id".equals(feiShuUserIdType)) {

            // 如果满足批量发送的用户类型则批量发送
            if (BATCH_MESSAGE_TYPE.contains(msgType)) {
                feiShuClient.sendMessageBatch(tenantAccessToken, sendTaskDto);
            } else { // 否则单个发送
                feiShuClient.sendMessage(tenantAccessToken, sendTaskDto, feiShuUserIdType);
            }
        } else if ("email".equals(feiShuUserIdType)) {

            // 邮箱类型只支持单个发送
            feiShuClient.sendMessage(tenantAccessToken, sendTaskDto, feiShuUserIdType);
        } else if ("chat_id".equals((feiShuUserIdType))) {

            // 群聊类型只支持单个发送
            feiShuClient.sendMessage(tenantAccessToken, sendTaskDto, feiShuUserIdType);
        } else if ("department_id".equals((feiShuUserIdType))) {

            // 参数的处理
            Object user_ids = paramMap.get("user_ids");
            paramMap.remove("user_ids");
            paramMap.put("department_ids", user_ids);
            // 部门只能批量
            feiShuClient.sendMessageBatch(tenantAccessToken, sendTaskDto);
        }
    }
}
