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

package cn.snowsoft.iot.module.message.validator;

import cn.hutool.json.JSONUtil;
import cn.snowsoft.iot.module.message.annotation.JsonString;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * JSON 校验
 *
 * @author oszero
 * @version 1.0.0
 */
public class JsonStringValidator implements ConstraintValidator<JsonString, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            // 尝试使用 Hutool 的 JSONUtil 解析字符串
            JSONUtil.parseObj(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

