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
import cn.snowsoft.iot.module.message.client.mail.MailClient;
import cn.snowsoft.iot.module.message.handler.BaseHandler;
import cn.snowsoft.iot.module.message.model.app.MailApp;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import org.springframework.stereotype.Component;

/**
 * 邮箱消费者处理器
 *
 * @author oszero
 * @version 1.0.0
 */
@Component
public class MailHandler extends BaseHandler {

    private final MailClient mailClient;

    public MailHandler(MailClient mailClient, MessageRecordService messageRecordService) {
        this.mailClient = mailClient;
        this.messageRecordService = messageRecordService;
    }

    @Override
    protected void handle(SendTaskDto sendTaskDto) throws Exception {
        String appConfigJson = sendTaskDto.getAppConfig();
        MailApp mailApp = JSONUtil.toBean(appConfigJson, MailApp.class);

        mailClient.sendMail(mailApp, sendTaskDto);
    }
}
