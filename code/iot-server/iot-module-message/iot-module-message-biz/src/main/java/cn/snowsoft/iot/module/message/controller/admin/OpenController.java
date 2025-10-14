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

package cn.snowsoft.iot.module.message.controller.admin;


import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.message.model.dto.request.SendRequestDto;
import cn.snowsoft.iot.module.message.service.SendService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 开放接口
 *
 * @author oszero
 * @version 1.0.0
 */
@Validated
@RestController
@RequestMapping("/message/open")
@RequiredArgsConstructor
public class OpenController {

    private final SendService sendService;

    /**
     * 发送消息
     *
     * @param sendRequestDto 发送请求 DTO
     * @return 返回成功信息
     */
    @PostMapping("/sendMessage")
    public CommonResult<String> sendMessage(@Valid @RequestBody SendRequestDto sendRequestDto) {
        String traceId = sendService.send(sendRequestDto);
        return CommonResult.success(traceId);
    }
}
