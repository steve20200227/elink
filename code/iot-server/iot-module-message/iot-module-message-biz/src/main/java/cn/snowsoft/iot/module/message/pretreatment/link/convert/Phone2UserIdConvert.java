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

package cn.snowsoft.iot.module.message.pretreatment.link.convert;

import cn.hutool.json.JSONUtil;
import cn.snowsoft.iot.module.message.cache.manager.ServerCacheManager;
import cn.snowsoft.iot.module.message.client.ding.DingClient;
import cn.snowsoft.iot.module.message.client.feishu.FeiShuClient;
import cn.snowsoft.iot.module.message.client.wechat.WeChatClient;
import cn.snowsoft.iot.module.message.model.app.DingApp;
import cn.snowsoft.iot.module.message.model.app.FeiShuApp;
import cn.snowsoft.iot.module.message.model.app.WeChatApp;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.pretreatment.common.LinkContext;
import cn.snowsoft.iot.module.message.pretreatment.common.MessageLink;
import cn.snowsoft.iot.module.message.pretreatment.constant.PretreatmentCodeConstant;
import cn.snowsoft.iot.module.message.util.MessageLinkTraceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 手机号转换平台 ID
 *
 * @author oszero
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class Phone2UserIdConvert implements MessageLink<SendTaskDto> {

    private final Map<String, Phone2UserId> strategyMap;

    @Override
    public void process(LinkContext<SendTaskDto> context) {
        SendTaskDto sendTaskDto = context.getProcessModel();
        List<String> users = sendTaskDto.getUsers();
        // 获取 appConfig
        String appConfigJson = sendTaskDto.getAppConfig();
        // 策略模式实现 phone 转换平台 userId
        Phone2UserId phone2UserId = strategyMap.get(context.getCode());
        sendTaskDto.setUsers(phone2UserId.convert(appConfigJson, users, sendTaskDto));

        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "完成手机号转换平台 ID");
    }

    /**
     * 手机号转 userId 接口
     */
    public interface Phone2UserId {
        /**
         * 转换
         *
         * @param appConfigJson app配置
         * @param phones        电话列表
         * @param sendTaskDto   发送任务
         * @return 用户ID列表
         */
        List<String> convert(String appConfigJson, List<String> phones, SendTaskDto sendTaskDto);
    }

    /**
     * 钉钉
     */
    @Component(PretreatmentCodeConstant.PHONE_DING)
    @RequiredArgsConstructor
    public static class DingStrategy implements Phone2UserId {
        private final DingClient dingClient;
        private final ServerCacheManager serverCacheManager;

        @Override
        public List<String> convert(String appConfigJson, List<String> phones, SendTaskDto sendTaskDto) {
            String accessToken = serverCacheManager.getDingToken(JSONUtil.toBean(appConfigJson, DingApp.class), sendTaskDto);
            return phones.stream().map(phone -> dingClient.getUserIdByPhone(accessToken, phone, sendTaskDto)).collect(Collectors.toList());
        }
    }

    /**
     * 企业微信
     */
    @Component(PretreatmentCodeConstant.PHONE_WECHAT)
    @RequiredArgsConstructor
    public static class WeChatStrategy implements Phone2UserId {
        private final WeChatClient weChatClient;
        private final ServerCacheManager serverCacheManager;

        @Override
        public List<String> convert(String appConfigJson, List<String> phones, SendTaskDto sendTaskDto) {
            String accessToken = serverCacheManager.getWeChatToken(JSONUtil.toBean(appConfigJson, WeChatApp.class), sendTaskDto);
            return weChatClient.getUserIdByPhone(accessToken, phones, sendTaskDto);
        }
    }

    /**
     * 飞书
     */
    @Component(PretreatmentCodeConstant.PHONE_FEI_SHU)
    @RequiredArgsConstructor
    public static class FeiShuStrategy implements Phone2UserId {
        private final FeiShuClient feiShuClient;
        private final ServerCacheManager serverCacheManager;

        @Override
        public List<String> convert(String appConfigJson, List<String> phones, SendTaskDto sendTaskDto) {
            String tenantAccessToken = serverCacheManager.getFeiShuToken(JSONUtil.toBean(appConfigJson, FeiShuApp.class), sendTaskDto);
            return feiShuClient.getUserIdsByPhones(tenantAccessToken, phones, sendTaskDto);
        }
    }
}
