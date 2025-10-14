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
import cn.snowsoft.iot.module.message.client.call.CallClient;
import cn.snowsoft.iot.module.message.client.call.factory.CallFactory;
import cn.snowsoft.iot.module.message.enums.CallProviderTypeEnum;
import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.handler.BaseHandler;
import cn.snowsoft.iot.module.message.model.app.call.AliYunCallApp;
import cn.snowsoft.iot.module.message.model.app.call.CallApp;
import cn.snowsoft.iot.module.message.model.app.call.TencentCallApp;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 电话消费者处理器
 *
 * @author oszero
 * @version 1.0.0
 */
@Component
public class CallHandler extends BaseHandler {

    private final CallFactory callFactory;
    private final static String CALL_PROVIDER = "callProvider";

    public CallHandler(CallFactory callFactory, MessageRecordService messageRecordService) {
        this.callFactory = callFactory;
        this.messageRecordService = messageRecordService;
    }

    @Override
    protected void handle(SendTaskDto sendTaskDto) throws Exception {
        try {
            Map<String, Object> map = sendTaskDto.getParamMap();

            String appConfig = sendTaskDto.getAppConfig();
            String callProvider = map.get(CALL_PROVIDER).toString();
            // 获取到具体电话应用
            CallApp callApp = getCallApp(callProvider, appConfig);
            // 获取到具体电话应用客户端
            CallClient callClient = callFactory.getClient(callProvider);
            // 发送电话消息
            callClient.sendCall(callApp, sendTaskDto);
        } catch (Exception e) {
            throw new MessageException(sendTaskDto, e.getMessage());
        }
    }

    private CallApp getCallApp(String callProvider, String appConfig) {

        if (CallProviderTypeEnum.ALI_YUN.getName().equals(callProvider)) {
            return JSONUtil.toBean(appConfig, AliYunCallApp.class);
        } else if (CallProviderTypeEnum.TENCENT.getName().equals(callProvider)) {
            return JSONUtil.toBean(appConfig, TencentCallApp.class);
        }
        throw new MessageException("没有指定的电话服务 App:" + callProvider);
    }
}
