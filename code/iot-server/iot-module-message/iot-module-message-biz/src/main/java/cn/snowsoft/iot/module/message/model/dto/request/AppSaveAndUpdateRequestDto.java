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

package cn.snowsoft.iot.module.message.model.dto.request;


import cn.snowsoft.iot.module.message.annotation.JsonString;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * APP 保存和更新 DTO
 *
 * @author oszero
 * @version 1.0.0
 */
@Data
public class AppSaveAndUpdateRequestDto {

    /**
     * appId
     */
    private Long appId;

    /**
     * 应用名称
     */
    @NotBlank
    @Length(min = 3, max = 20)
    private String appName;

    /**
     * 消息发送渠道类型 （1-打电话 2-发短信 3-邮件 4-企业微信 5-钉钉 6-飞书）
     */
    @NotNull
    private Integer channelType;

    /**
     * 应用信息配置 json
     */
    @NotBlank
    @JsonString
    private String appConfig;

    /**
     * APP 状态
     */
    @NotNull
    private Integer appStatus;
}
