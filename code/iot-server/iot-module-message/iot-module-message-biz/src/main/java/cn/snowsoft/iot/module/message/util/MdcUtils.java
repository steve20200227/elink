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

package cn.snowsoft.iot.module.message.util;

import org.slf4j.MDC;

/**
 * MDC 工具类
 *
 * @author oszero
 * @version 1.0.0
 */
public class MdcUtils {

    /**
     * 设置 MDC
     *
     * @param key   键
     * @param value 值
     */
    public static void put(String key, String value) {
        MDC.put(key, value);
    }

    /**
     * 获取 MDC 中的数据
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        return MDC.get(key);
    }

    /**
     * 清空 MDC
     */
    public static void clear() {
        MDC.clear();
    }
}
