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

package cn.snowsoft.iot.module.message.mq.consumer.nomq;


import cn.snowsoft.iot.module.message.handler.BaseHandler;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.model.event.BaseApplicationEvent;
import cn.snowsoft.iot.module.message.mq.consumer.common.MQCommonConsumer;
import cn.snowsoft.iot.module.message.mq.producer.Producer;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 无 MQ 通用事件监听器
 *
 * @author oszero
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "mq-type", havingValue = "none")
public class NoMQCommonListener {

    private final MessageRecordService messageRecordService;
    private final Producer producer;

    /**
     * 消息确认
     *
     * @param event   事件
     * @param handler 渠道处理器
     */
    public void omMessageAck(BaseApplicationEvent event, BaseHandler handler) {
        SendTaskDto sendTaskDto = event.getSendTaskDto();
        try {
            MQCommonConsumer.tryHandle(sendTaskDto, handler);
        } catch (Exception exception) {
            MQCommonConsumer.catchHandle(sendTaskDto, exception, messageRecordService, producer);
        }
    }
}
