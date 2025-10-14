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

package cn.snowsoft.iot.module.message.pretreatment.link.paramcheck.wechat;

import cn.hutool.core.util.StrUtil;
import cn.snowsoft.iot.module.message.enums.MessageTypeEnum;
import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.pretreatment.common.LinkContext;
import cn.snowsoft.iot.module.message.pretreatment.common.MessageLink;
import cn.snowsoft.iot.module.message.util.MessageLinkTraceUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 企微通用参数校验
 *
 * @author oszero
 * @version 1.0.0
 */
@Service
public class WeChatCommonParamCheck implements MessageLink<SendTaskDto> {

    private static final Set<String> WECHAT_APP_USER_ID_TYPE_SET = new HashSet<>(
            Arrays.asList("touser", "toparty", "totag"));
    private static final Set<String> WECHAT_SCHOOL_APP_USER_ID_TYPE_SET = new HashSet<>(
            Arrays.asList("to_parent_userid", "to_student_userid", "to_party", "toall"));

    /**
     * 可以通过企微家校发送的消息类型
     */
    private static final Set<String> SCHOOL_APP_MESSAGE_TYPE =
            new HashSet<>(Arrays.asList(
                    MessageTypeEnum.TEXT.getCode(),
                    MessageTypeEnum.WECHAT_IMAGE.getCode(),
                    MessageTypeEnum.WECHAT_VOICE.getCode(),
                    MessageTypeEnum.WECHAT_VIDEO.getCode(),
                    MessageTypeEnum.WECHAT_FILE.getCode(),
                    MessageTypeEnum.WECHAT_MPNEWS.getCode(),
                    MessageTypeEnum.WECHAT_MINIPROGRAM_NOTICE.getCode()
            ));

    /**
     * 可以通过企微应用群聊发送的消息类型
     */
    private static final Set<String> CHAT_APP_MESSAGE_TYPE =
            new HashSet<>(Arrays.asList(
                    MessageTypeEnum.TEXT.getCode(),
                    MessageTypeEnum.WECHAT_IMAGE.getCode(),
                    MessageTypeEnum.WECHAT_VOICE.getCode(),
                    MessageTypeEnum.WECHAT_VIDEO.getCode(),
                    MessageTypeEnum.WECHAT_FILE.getCode(),
                    MessageTypeEnum.WECHAT_TEXT_CARD.getCode(),
                    MessageTypeEnum.WECHAT_MPNEWS.getCode(),
                    MessageTypeEnum.WECHAT_MARKDOWN.getCode()
            ));

    /**
     * 可以通过企微群机器人发送的消息类型
     */
    private static final Set<String> ROBOT_MESSAGE_TYPE =
            new HashSet<>(Arrays.asList(
                    MessageTypeEnum.TEXT.getCode(),
                    MessageTypeEnum.WECHAT_MARKDOWN.getCode(),
                    MessageTypeEnum.WECHAT_IMAGE.getCode(),
                    MessageTypeEnum.WECHAT_FILE.getCode(),
                    MessageTypeEnum.WECHAT_VOICE.getCode(),
                    MessageTypeEnum.WECHAT_TEMPLATE_CARD.getCode()
            ));

    @Override
    public void process(LinkContext<SendTaskDto> context) {
        SendTaskDto sendTaskDto = context.getProcessModel();
        String messageType = sendTaskDto.getMessageType();
        Map<String, Object> paramMap = sendTaskDto.getParamMap();

        String pushSubject = paramMap.getOrDefault("pushSubject", "").toString();
        String wechatUserIdType = paramMap.getOrDefault("wechatUserIdType", "").toString();

        if (StrUtil.isBlank(pushSubject) || StrUtil.isBlank(wechatUserIdType)) {
            throw new MessageException(sendTaskDto, "企微 pushSubject 或者 wechatUserIdType 参数为空");
        }

        if (!("app".equals(pushSubject) || "robot".equals(pushSubject))) {
            throw new MessageException(sendTaskDto, "企微 pushSubject 非法，必须为 app 或者 robot");
        }

        if ("app".equals(pushSubject)) {
            if (!(WECHAT_APP_USER_ID_TYPE_SET.contains(wechatUserIdType)
                    || WECHAT_SCHOOL_APP_USER_ID_TYPE_SET.contains(wechatUserIdType)
                    || "chatid".equals(wechatUserIdType))) {
                throw new MessageException(sendTaskDto, "企微 pushSubject 为 app 时，wechatUserIdType 参数必须为 touser 或者 toparty 或者 totag 或者 to_parent_userid 或者 to_student_userid 或者 to_party 或者 toall 或者 chatid");
            }

            if (WECHAT_SCHOOL_APP_USER_ID_TYPE_SET.contains(wechatUserIdType)) {
                if (!SCHOOL_APP_MESSAGE_TYPE.contains(messageType)) {
                    throw new MessageException(sendTaskDto, "当 wechatUserIdType 为 to_parent_userid 或者 to_student_userid 或者 to_party 或者 toall 时，不支持此消息类型");
                }
            } else {
                if (!CHAT_APP_MESSAGE_TYPE.contains(messageType)) {
                    throw new MessageException(sendTaskDto, "当 wechatUserIdType 为 chatid 时，不支持此消息类型");
                }
            }
        } else {
            if (!"webhook".equals(wechatUserIdType)) {
                throw new MessageException(sendTaskDto, "企微 pushSubject 为 robot 时，wechatUserIdType 参数必须为 webhook");
            }

            if (!ROBOT_MESSAGE_TYPE.contains(messageType)) {
                throw new MessageException(sendTaskDto, "当 wechatUserIdType 为 webhook 时，不支持此消息类型");
            }
        }

        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "完成企微通用参数校验");
    }
}
