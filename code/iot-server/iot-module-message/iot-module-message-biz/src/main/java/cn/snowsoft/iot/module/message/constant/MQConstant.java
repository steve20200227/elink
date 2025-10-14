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

package cn.snowsoft.iot.module.message.constant;

/**
 * MQ 常量接口
 *
 * @author oszero
 * @version 1.0.0
 */
public interface MQConstant {
    /**
     * RocketMQ
     */
    String CALL_TOPIC = "topic_msg_call";
    String DING_TOPIC = "topic_msg_ding";
    String FEI_SHU_TOPIC = "topic_msg_feiShu";
    String MAIL_TOPIC = "topic_msg_mail";
    String SMS_TOPIC = "topic_msg_sms";
    String WECHAT_TOPIC = "topic_msg_wechat";

    String CALL_CONSUMER_GROUP = "consumer_group_msg_call";
    String DING_CONSUMER_GROUP = "consumer_group_msg_ding";
    String FEI_SHU_CONSUMER_GROUP = "consumer_group_msg_feiShu";
    String MAIL_CONSUMER_GROUP = "consumer_group_msg_mail";
    String SMS_CONSUMER_GROUP = "consumer_group_msg_sms";
    String WECHAT_CONSUMER_GROUP = "consumer_group_msg_wechat";

    /**
     * RabbitMQ
     */
    String DELIVER_EXCHANGE = "deliver_exchange";

    String CALL_QUEUE = "queue_msg_call";
    String DING_QUEUE = "queue_msg_ding";
    String FEI_SHU_QUEUE = "queue_msg_feiShu";
    String MAIL_QUEUE = "queue_msg_mail";
    String SMS_QUEUE = "queue_msg_sms";
    String WECHAT_QUEUE = "queue_msg_wechat";

    String CALL_KEY_NAME = "key_msg_call";
    String DING_KEY_NAME = "key_msg_ding";
    String FEI_SHU_KEY_NAME = "key_msg_feiShu";
    String MAIL_KEY_NAME = "key_msg_mail";
    String SMS_KEY_NAME = "key_msg_sms";
    String WECHAT_KEY_NAME = "key_msg_wechat";

    /**
     * Redis Stream
     */
    String CALL_STREAM = "call_stream";
    String SMS_STREAM = "sms_stream";
    String MAIL_STREAM = "mail_stream";
    String DING_STREAM = "ding_stream";
    String WECHAT_STREAM = "wechat_stream";
    String FEI_SHU_STREAM = "feiShu_stream";

    String CALL_STREAM_CONSUMER_GROUP = "stream_consumer_group_msg_call";
    String SMS_STREAM_CONSUMER_GROUP = "stream_consumer_group_msg_sms";
    String MAIL_STREAM_CONSUMER_GROUP = "stream_consumer_group_msg_mail";
    String DING_STREAM_CONSUMER_GROUP = "stream_consumer_group_msg_ding";
    String WECHAT_STREAM_CONSUMER_GROUP = "stream_consumer_group_msg_wechat";
    String FEI_SHU_STREAM_CONSUMER_GROUP = "stream_consumer_group_msg_feiShu";

    String CALL_STREAM_CONSUMER_NAME = "stream_consumer_name_msg_call";
    String SMS_STREAM_CONSUMER_NAME = "stream_consumer_name_msg_sms";
    String MAIL_STREAM_CONSUMER_NAME = "stream_consumer_name_msg_mail";
    String DING_STREAM_CONSUMER_NAME = "stream_consumer_name_msg_ding";
    String WECHAT_STREAM_CONSUMER_NAME = "stream_consumer_name_msg_wechat";
    String FEI_SHU_STREAM_CONSUMER_NAME = "stream_consumer_name_msg_feiShu";

    /**
     * kafka
     */
    String TOPIC = "";
}
