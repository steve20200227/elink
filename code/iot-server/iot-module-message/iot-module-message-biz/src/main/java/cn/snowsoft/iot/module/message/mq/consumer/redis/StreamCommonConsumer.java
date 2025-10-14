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

package cn.snowsoft.iot.module.message.mq.consumer.redis;

import cn.hutool.json.JSONUtil;
import cn.snowsoft.iot.module.message.constant.MQConstant;
import cn.snowsoft.iot.module.message.handler.BaseHandler;
import cn.snowsoft.iot.module.message.handler.impl.*;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.mq.consumer.common.MQCommonConsumer;
import cn.snowsoft.iot.module.message.mq.producer.Producer;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * StreamCommonConsumer
 *
 * @author oszero
 * @version 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "mq-type", havingValue = "redis")
public class StreamCommonConsumer {
    private final MessageRecordService messageRecordService;
    private final Producer producer;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 确认消息
     *
     * @param message 消息
     * @param handler 渠道处理器
     */
    public void omMessageAck(ObjectRecord<String, String> message, BaseHandler handler) {
        SendTaskDto sendTaskDto = null;
        try {
            sendTaskDto = JSONUtil.toBean(message.getValue(), SendTaskDto.class);
            MQCommonConsumer.tryHandle(sendTaskDto, handler);

            // 确认消息消费成功
            ack(message, handler);
        } catch (Exception exception) {
            // 确认消息消费成功
            ack(message, handler);
            MQCommonConsumer.catchHandle(sendTaskDto, exception, messageRecordService, producer);
        }
    }

    /**
     * ACK
     *
     * @param message 消息
     * @param handler 渠道处理器
     */
    private void ack(ObjectRecord<String, String> message, BaseHandler handler) {
        // 消息确认
        Long acknowledge = 0L;
        if (handler instanceof CallHandler) {
            acknowledge = stringRedisTemplate.opsForStream().acknowledge(MQConstant.CALL_STREAM,
                    MQConstant.CALL_STREAM_CONSUMER_GROUP,
                    message.getId());
        } else if (handler instanceof SmsHandler) {
            acknowledge = stringRedisTemplate.opsForStream().acknowledge(MQConstant.SMS_STREAM,
                    MQConstant.SMS_STREAM_CONSUMER_GROUP,
                    message.getId());
        } else if (handler instanceof MailHandler) {
            acknowledge = stringRedisTemplate.opsForStream().acknowledge(MQConstant.MAIL_STREAM,
                    MQConstant.MAIL_STREAM_CONSUMER_GROUP,
                    message.getId());
        } else if (handler instanceof DingHandler) {
            acknowledge = stringRedisTemplate.opsForStream().acknowledge(MQConstant.DING_STREAM,
                    MQConstant.DING_STREAM_CONSUMER_GROUP,
                    message.getId());
        } else if (handler instanceof WeChatHandler) {
            acknowledge = stringRedisTemplate.opsForStream().acknowledge(MQConstant.WECHAT_STREAM,
                    MQConstant.WECHAT_STREAM_CONSUMER_GROUP,
                    message.getId());
        } else if (handler instanceof FeiShuHandler) {
            acknowledge = stringRedisTemplate.opsForStream().acknowledge(MQConstant.FEI_SHU_STREAM,
                    MQConstant.FEI_SHU_STREAM_CONSUMER_GROUP,
                    message.getId());
        }
        if (Objects.equals(acknowledge, 1L)) {
            log.info("Redis Stream 消息已确认消费");
        } else {
            log.error("Redis Stream 消息确认失败");
        }
    }
}
