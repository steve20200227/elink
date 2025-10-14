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

package cn.snowsoft.iot.module.message.mq.consumer.rabbitmq;

import cn.hutool.json.JSONUtil;
import cn.snowsoft.iot.module.message.constant.MQConstant;
import cn.snowsoft.iot.module.message.handler.BaseHandler;
import cn.snowsoft.iot.module.message.handler.impl.*;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.mq.consumer.common.MQCommonConsumer;
import cn.snowsoft.iot.module.message.mq.producer.Producer;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * RabbitMQConsumer
 *
 * @author oszero
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "mq-type", havingValue = "rabbitmq")
public class RabbitMQConsumer {

    /**
     * 各处理器
     */
    private final CallHandler callHandler;
    private final SmsHandler smsHandler;
    private final MailHandler mailHandler;
    private final DingHandler dingHandler;
    private final WeChatHandler weChatHandler;
    private final FeiShuHandler feiShuHandler;

    /**
     * 消息记录服务
     */
    private final MessageRecordService messageRecordService;

    /**
     * 生产者
     */
    private final Producer producer;

    /**
     * 消费电话队列消息
     *
     * @param message     消息 json SendTaskDto
     * @param deliveryTag 消息标识
     * @param channel     与 RabbitMQ 连接的 Channel
     */
    @RabbitListener(queues = MQConstant.CALL_QUEUE, ackMode = "MANUAL")
    public void onCallMessage(String message,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        onMessageAck(deliveryTag, channel, message, callHandler);
    }

    /**
     * 消费短信队列消息
     *
     * @param message     消息 json SendTaskDto
     * @param deliveryTag 消息标识
     * @param channel     与 RabbitMQ 连接的 Channel
     */
    @RabbitListener(queues = MQConstant.SMS_QUEUE, ackMode = "MANUAL")
    public void onSmsMessage(String message,
                             @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        onMessageAck(deliveryTag, channel, message, smsHandler);
    }

    /**
     * 消费邮件队列消息
     *
     * @param message     消息 json SendTaskDto
     * @param deliveryTag 消息标识
     * @param channel     与 RabbitMQ 连接的 Channel
     */
    @RabbitListener(queues = MQConstant.MAIL_QUEUE, ackMode = "MANUAL")
    public void onMailMessage(String message,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        onMessageAck(deliveryTag, channel, message, mailHandler);
    }

    /**
     * 消费钉钉队列消息
     *
     * @param message     消息 json SendTaskDto
     * @param deliveryTag 消息标识
     * @param channel     与 RabbitMQ 连接的 Channel
     */
    @RabbitListener(queues = MQConstant.DING_QUEUE, ackMode = "MANUAL")
    public void onDingMessage(String message,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        onMessageAck(deliveryTag, channel, message, dingHandler);
    }

    /**
     * 消费企业微信队列消息
     *
     * @param message     消息 json SendTaskDto
     * @param deliveryTag 消息标识
     * @param channel     与 RabbitMQ 连接的 Channel
     */
    @RabbitListener(queues = MQConstant.WECHAT_QUEUE, ackMode = "MANUAL")
    public void onWeChatMessage(String message,
                                @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        onMessageAck(deliveryTag, channel, message, weChatHandler);
    }

    /**
     * 消费飞书队列消息
     *
     * @param message     消息 json SendTaskDto
     * @param deliveryTag 消息标识
     * @param channel     与 RabbitMQ 连接的 Channel
     */
    @RabbitListener(queues = MQConstant.FEI_SHU_QUEUE, ackMode = "MANUAL")
    public void onFeiShuMessage(String message,
                                @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws Exception {
        onMessageAck(deliveryTag, channel, message, feiShuHandler);
    }

    private void onMessageAck(long deliveryTag, Channel channel, String message, BaseHandler handler) throws Exception {

        SendTaskDto sendTaskDto = null;
        try {
            sendTaskDto = JSONUtil.toBean(message, SendTaskDto.class);
            MQCommonConsumer.tryHandle(sendTaskDto, handler);

            // RabbitMQ 的 ack 机制中，第二个参数返回 true，表示需要将这条消息投递给其他的消费者重新消费
            channel.basicAck(deliveryTag, false);
        } catch (Exception exception) {
            channel.basicAck(deliveryTag, false);
            // 报错重试
            MQCommonConsumer.catchHandle(sendTaskDto, exception, messageRecordService, producer);
        }
    }

}
