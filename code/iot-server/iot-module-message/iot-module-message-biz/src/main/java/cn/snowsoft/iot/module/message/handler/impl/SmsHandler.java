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

import cn.snowsoft.iot.module.message.client.sms.SmsClient;
import cn.snowsoft.iot.module.message.client.sms.factory.SmsFactory;
import cn.snowsoft.iot.module.message.enums.SmsProviderTypeEnum;
import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.handler.BaseHandler;
import cn.snowsoft.iot.module.message.model.app.sms.AliYunSmsApp;
import cn.snowsoft.iot.module.message.model.app.sms.SmsApp;
import cn.snowsoft.iot.module.message.model.app.sms.TencentSmsApp;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信消费者处理器
 *
 * @author oszero
 * @version 1.0.0
 */
@Component
public class SmsHandler extends BaseHandler {

    private static final String SMS_PROVIDER = "smsProvider";

    private final SmsFactory smsFactory;

    public SmsHandler(SmsFactory smsFactory, MessageRecordService messageRecordService) {
        this.smsFactory = smsFactory;
        this.messageRecordService = messageRecordService;
    }

    @Override
    protected void handle(SendTaskDto sendTaskDto) throws Exception {
        try {
            Map<String, Object> map = sendTaskDto.getParamMap();
            String appConfig = sendTaskDto.getAppConfig();
            String smsProvider = map.get(SMS_PROVIDER).toString();
            SmsApp smsApp = getSmsApp(smsProvider, appConfig);
            SmsClient smsClient = smsFactory.getClient(smsProvider);
            smsClient.sendSms(smsApp, sendTaskDto);
        } catch (Exception e) {
            throw new MessageException(sendTaskDto, e.getMessage());
        }
    }

    private SmsApp getSmsApp(String smsProvider, String appConfig) {

        if (SmsProviderTypeEnum.ALI_YUN.getName().equals(smsProvider)) {
            return JSONUtil.toBean(appConfig, AliYunSmsApp.class);
        } else if (SmsProviderTypeEnum.TENCENT.getName().equals(smsProvider)) {
            return JSONUtil.toBean(appConfig, TencentSmsApp.class);
        }
        throw new MessageException("没有指定的短信服务 App:" + smsProvider);
    }
}
