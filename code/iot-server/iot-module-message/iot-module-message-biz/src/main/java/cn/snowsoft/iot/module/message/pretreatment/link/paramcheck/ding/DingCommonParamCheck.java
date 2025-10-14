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

package cn.snowsoft.iot.module.message.pretreatment.link.paramcheck.ding;

import cn.hutool.core.util.StrUtil;
import cn.snowsoft.iot.module.message.enums.MessageTypeEnum;
import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.pretreatment.common.LinkContext;
import cn.snowsoft.iot.module.message.pretreatment.common.MessageLink;
import cn.snowsoft.iot.module.message.util.MessageLinkTraceUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 钉钉通用参数校验
 *
 * @author oszero
 * @version 1.0.0
 */
@Component
public class DingCommonParamCheck implements MessageLink<SendTaskDto> {

    @Override
    public void process(LinkContext<SendTaskDto> context) {
        SendTaskDto sendTaskDto = context.getProcessModel();
        String messageType = sendTaskDto.getMessageType();
        Map<String, Object> paramMap = sendTaskDto.getParamMap();

        String pushSubject = paramMap.getOrDefault("pushSubject", "").toString();
        String dingUserIdType = paramMap.getOrDefault("dingUserIdType", "").toString();

        if (StrUtil.isBlank(pushSubject) || StrUtil.isBlank(dingUserIdType)) {
            throw new MessageException(sendTaskDto, "钉钉 pushSubject 或者 dingUserIdType 参数为空");
        }

        if (!("workNotice".equals(pushSubject) || "robot".equals(pushSubject))) {
            throw new MessageException(sendTaskDto, "钉钉 pushSubject 非法，必须为 workNotice 或者 robot");
        }

        if ("workNotice".equals(pushSubject)) {
            if (!("userid_list".equals(dingUserIdType) || "dept_id_list".equals(dingUserIdType))) {
                throw new MessageException(sendTaskDto, "当 pushSubject 为 workNotice 时，dingUserIdType 必须为 userid_list 或者 dept_id_list");
            }
        } else {
            if (!("userIds".equals(dingUserIdType) || "openConversationId".equals(dingUserIdType))) {
                throw new MessageException(sendTaskDto, "当 pushSubject 为 robot 时，dingUserIdType 必须为 userIds 或者 openConversationId");
            }

            if (MessageTypeEnum.DING_OA.getCode().equals(messageType)) {
                throw new MessageException(sendTaskDto, "钉钉 pushSubject 为 robot 时，不支持 OA 消息类型");
            }
        }

        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "完成钉钉通用参数校验");
    }
}
