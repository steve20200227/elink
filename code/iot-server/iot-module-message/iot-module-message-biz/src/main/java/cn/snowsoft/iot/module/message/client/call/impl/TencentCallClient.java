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

package cn.snowsoft.iot.module.message.client.call.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import cn.snowsoft.iot.module.message.client.call.CallClient;
import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.model.app.call.CallApp;
import cn.snowsoft.iot.module.message.model.app.call.TencentCallApp;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.vms.v20200902.VmsClient;
import com.tencentcloudapi.vms.v20200902.models.SendStatus;
import com.tencentcloudapi.vms.v20200902.models.SendTtsVoiceRequest;
import com.tencentcloudapi.vms.v20200902.models.SendTtsVoiceResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 腾讯云电话客户端
 *
 * @author oszero
 * @version 1.0.0
 */
@Component
public class TencentCallClient implements CallClient {
    @Override
    public void sendCall(CallApp callApp, SendTaskDto sendTaskDto) throws Exception {
        try {
            TencentCallApp tencentCallApp = (TencentCallApp) callApp;
            String secretId = tencentCallApp.getSecretId();
            String secretKey = tencentCallApp.getSecretKey();
            Credential cred = new Credential(secretId, secretKey);

            final List<String> users = sendTaskDto.getUsers();
            // 获取腾讯云语音通知相关参数
            Map<String, Object> paramMap = sendTaskDto.getParamMap();
            final String region = paramMap.get("region").toString();
            final String templateId = paramMap.get("templateId").toString();
            final List<String> templateParamSet = (List<String>) paramMap.getOrDefault("templateParamSet", new ArrayList<String>());
            final Long playTimes = (Long) paramMap.getOrDefault("playTimes", -1L);
            final String sessionContext = paramMap.getOrDefault("sessionContext", "").toString();
            final String voiceSdkAppid = paramMap.get("voiceSdkAppid").toString();

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("vms.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            final VmsClient client = new VmsClient(cred, region, clientProfile);

            SendTtsVoiceRequest req = new SendTtsVoiceRequest();
            // 设置腾讯云语音电话所需参数
            req.setTemplateId(templateId);
            if (CollectionUtil.isNotEmpty(templateParamSet)) {
                String[] templateParamSetArray = templateParamSet.toArray(new String[0]);
                req.setTemplateParamSet(templateParamSetArray);
            }
            req.setCalledNumber(users.get(0));
            if (!Objects.equals(playTimes, -1L)) {
                req.setPlayTimes(playTimes);
            }
            if (StrUtil.isNotBlank(sessionContext)) {
                req.setSessionContext(sessionContext);
            }
            req.setVoiceSdkAppid(voiceSdkAppid);

            SendTtsVoiceResponse resp = client.SendTtsVoice(req);
            SendStatus sendStatus = resp.getSendStatus();

        } catch (Exception e) {
            throw new MessageException("腾讯云语音电话消息发送失败，" + e.getMessage());
        }
    }
}
