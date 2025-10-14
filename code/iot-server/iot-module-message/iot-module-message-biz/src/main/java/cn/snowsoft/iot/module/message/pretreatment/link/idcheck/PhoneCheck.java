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

package cn.snowsoft.iot.module.message.pretreatment.link.idcheck;

import cn.hutool.core.lang.Validator;
import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.pretreatment.common.LinkContext;
import cn.snowsoft.iot.module.message.pretreatment.common.MessageLink;
import cn.snowsoft.iot.module.message.util.MessageLinkTraceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 手机号检查
 *
 * @author oszero
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class PhoneCheck implements MessageLink<SendTaskDto> {

    @Override
    public void process(LinkContext<SendTaskDto> context) {
        SendTaskDto sendTaskDto = context.getProcessModel();
        List<String> users = sendTaskDto.getUsers();
        users.forEach(phone -> {
            if (!Validator.isMobile(phone)) {
                throw new MessageException(sendTaskDto, "[PhoneCheck#process]错误：消息接收者中有非[电话号码]用户");
            }
        });
        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "完成手机号检查");
    }
}
