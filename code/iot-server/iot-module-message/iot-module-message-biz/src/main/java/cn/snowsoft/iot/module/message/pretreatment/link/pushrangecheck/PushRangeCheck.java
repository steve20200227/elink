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

package cn.snowsoft.iot.module.message.pretreatment.link.pushrangecheck;


import cn.snowsoft.iot.module.message.enums.ChannelTypeEnum;
import cn.snowsoft.iot.module.message.enums.PushRangeEnum;
import cn.snowsoft.iot.module.message.enums.UsersTypeEnum;
import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.pretreatment.common.LinkContext;
import cn.snowsoft.iot.module.message.pretreatment.common.MessageLink;
import cn.snowsoft.iot.module.message.util.MessageLinkTraceUtils;
import org.springframework.stereotype.Service;

/**
 * 推送范围检测
 *
 * @author oszero
 * @version 1.0.0
 */
@Service
public class PushRangeCheck implements MessageLink<SendTaskDto> {

    /**
     * 真正处理逻辑
     *
     * @param context 处理上下文
     */
    @Override
    public void process(LinkContext<SendTaskDto> context) {
        SendTaskDto sendTaskDto = context.getProcessModel();
        Integer pushRange = sendTaskDto.getPushRange();
        Integer usersType = sendTaskDto.getUsersType();
        Integer channelType = sendTaskDto.getChannelType();
        if (PushRangeEnum.ALL.getCode().equals(pushRange) || PushRangeEnum.INNER.getCode().equals(pushRange)) {
            // 企业直接为 true
            boolean company = UsersTypeEnum.COMPANY_ACCOUNT.getCode().equals(usersType);
            // 电话支持打电话、发短信、钉钉、企微、飞书
            boolean phone = UsersTypeEnum.PHONE.getCode().equals(usersType)
                    && (ChannelTypeEnum.CALL.getCode().equals(channelType)
                    || ChannelTypeEnum.SMS.getCode().equals(channelType)
                    || ChannelTypeEnum.DING.getCode().equals(channelType)
                    || ChannelTypeEnum.WECHAT.getCode().equals(channelType)
                    || ChannelTypeEnum.FEI_SHU.getCode().equals(channelType));
            // 邮箱支持发邮件
            boolean mail = UsersTypeEnum.MAIL.getCode().equals(usersType)
                    && ChannelTypeEnum.MAIL.getCode().equals(channelType);
            // 平台 ID 支持钉钉、企微、飞书
            boolean userId = UsersTypeEnum.PLATFORM_USER_ID.getCode().equals(usersType)
                    && (ChannelTypeEnum.DING.getCode().equals(channelType)
                    || ChannelTypeEnum.WECHAT.getCode().equals(channelType)
                    || ChannelTypeEnum.FEI_SHU.getCode().equals(channelType));
            // 任一一个失败则表示配置错误
            if (!(company || phone || mail || userId)) {
                throw new MessageException(sendTaskDto, "推送范围错误");
            }
        } else { // 企业外部只有电话与邮箱
            // 电话支持打电话、发短信
            boolean phone = UsersTypeEnum.PHONE.getCode().equals(usersType)
                    && (ChannelTypeEnum.CALL.getCode().equals(channelType)
                    || ChannelTypeEnum.SMS.getCode().equals(channelType));
            // 邮箱支持发邮件
            boolean mail = UsersTypeEnum.MAIL.getCode().equals(usersType) && ChannelTypeEnum.MAIL.getCode().equals(channelType);
            // 任一一个失败则表示配置错误
            if (!(mail || phone)) {
                throw new MessageException(sendTaskDto, "推送范围错误");
            }
        }

        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "完成推送范围检查");
    }
}
