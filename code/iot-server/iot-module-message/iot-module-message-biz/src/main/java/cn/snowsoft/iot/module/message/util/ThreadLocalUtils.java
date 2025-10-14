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

import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.model.entity.UserInfo;


/**
 * ThreadLocal 工具类
 *
 * @author oszero
 * @version 1.0.0
 */
public class ThreadLocalUtils {
    private final static ThreadLocal<UserInfo> USER_INFO_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置用户信息
     *
     * @param userInfo 用户信息
     */
    public static void setUserInfo(UserInfo userInfo) {
        USER_INFO_THREAD_LOCAL.set(userInfo);
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static UserInfo getUserInfo() {
        return USER_INFO_THREAD_LOCAL.get();
    }

    /**
     * 清空 ThreadLocal
     */
    public static void clear() {
        USER_INFO_THREAD_LOCAL.remove();
        SEND_TASK_DTO_THREAD_LOCAL.remove();
    }


    private final static ThreadLocal<SendTaskDto> SEND_TASK_DTO_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 设置发送任务
     *
     * @param sendTaskDto 发送任务
     */
    public static void setSendTaskDto(SendTaskDto sendTaskDto) {
        SEND_TASK_DTO_THREAD_LOCAL.set(sendTaskDto);
    }

    /**
     * 获取发送任务
     *
     * @return 发送任务
     */
    public static SendTaskDto getSendTaskDto() {
        return SEND_TASK_DTO_THREAD_LOCAL.get();
    }


}
