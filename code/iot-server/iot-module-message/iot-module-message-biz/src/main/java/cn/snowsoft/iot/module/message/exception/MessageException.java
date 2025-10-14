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

package cn.snowsoft.iot.module.message.exception;


import cn.snowsoft.iot.module.message.enums.ResultEnum;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.util.MessageLinkTraceUtils;
import lombok.Getter;

/**
 * 消息链路异常
 *
 * @author oszero
 * @version 1.0.0
 */
@Getter
public class MessageException extends RuntimeException {
    private String message;
    private ResultEnum code;

    public MessageException() {
        super();
    }

    public MessageException(String message) {
        super(message);
        this.message = message;
        this.code = ResultEnum.ERROR;
    }

    public MessageException(SendTaskDto sendTaskDto, String message) {
        super(message);
        this.message = MessageLinkTraceUtils.formatMessageLifecycleErrorLogMsg(sendTaskDto, message) + "！！！";
        sendTaskDto.setFaliReason(message);
        this.code = ResultEnum.ERROR;
    }

    public MessageException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum;
    }
}
