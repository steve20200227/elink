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

package cn.snowsoft.iot.module.message.pretreatment.link.paramcheck.feishu;


import cn.snowsoft.iot.module.message.exception.MessageException;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.pretreatment.common.LinkContext;
import cn.snowsoft.iot.module.message.pretreatment.common.MessageLink;
import cn.snowsoft.iot.module.message.pretreatment.link.paramcheck.ParamStrategy;
import cn.snowsoft.iot.module.message.util.MessageLinkTraceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 飞书消息参数校验
 *
 * @author oszero
 * @version 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeiShuParamCheck implements MessageLink<SendTaskDto> {

    private final Map<String, ParamStrategy> feiShuParamStrategyMap;

    @Override
    public void process(LinkContext<SendTaskDto> context) {
        SendTaskDto sendTaskDto = context.getProcessModel();
        // 飞书 userId 存在请求参数里
        sendTaskDto.getParamMap().put("user_ids", sendTaskDto.getUsers());

        try {
            String strategyBeanName = ParamStrategy.FEI_SHU_STRATEGY_BEAN_PRE_NAME + sendTaskDto.getMessageType();
            ParamStrategy paramStrategy = feiShuParamStrategyMap.get(strategyBeanName);
            paramStrategy.paramCheck(sendTaskDto);
        } catch (Exception exception) {
            throw new MessageException(sendTaskDto, "飞书消息参数校验失败，" + exception.getMessage());
        }

        MessageLinkTraceUtils.recordMessageLifecycleInfoLog(sendTaskDto, "完成飞书参数校验");
    }
}
