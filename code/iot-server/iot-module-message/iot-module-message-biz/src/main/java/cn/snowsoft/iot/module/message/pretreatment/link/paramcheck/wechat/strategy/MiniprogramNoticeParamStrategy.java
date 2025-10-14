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

package cn.snowsoft.iot.module.message.pretreatment.link.paramcheck.wechat.strategy;


import cn.snowsoft.iot.module.message.enums.MessageTypeEnum;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.pretreatment.link.paramcheck.ParamStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 企微小程序通知消息校验
 *
 * @author oszero
 * @version 1.0.0
 */
@Component(ParamStrategy.WECHAT_STRATEGY_BEAN_PRE_NAME + "5-8")
public class MiniprogramNoticeParamStrategy implements ParamStrategy {
    @Override
    public void paramCheck(SendTaskDto sendTaskDto) throws Exception {
        Map<String, Object> paramMap = sendTaskDto.getParamMap();
        paramMap.put("msgtype", MessageTypeEnum.WECHAT_MINIPROGRAM_NOTICE.getMsgType());
    }
}
