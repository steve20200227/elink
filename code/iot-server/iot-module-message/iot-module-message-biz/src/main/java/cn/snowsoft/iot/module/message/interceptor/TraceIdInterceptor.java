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

package cn.snowsoft.iot.module.message.interceptor;


import cn.snowsoft.iot.module.message.constant.TraceIdConstant;
import cn.snowsoft.iot.module.message.log.trace.TraceIdStrategy;
import cn.snowsoft.iot.module.message.util.MdcUtils;
import cn.snowsoft.iot.module.message.util.ThreadLocalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * traceId 拦截器
 *
 * @author oszero
 * @version 1.0.0
 */
@Component
@RequiredArgsConstructor
public class TraceIdInterceptor implements HandlerInterceptor {

    private final TraceIdStrategy traceIdStrategy;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(TraceIdConstant.TRACE_ID);
        if (!Objects.isNull(traceId)) {
            //  此处是为了适配网关
            MdcUtils.put(TraceIdConstant.TRACE_ID, traceId);
        } else {
            String trace = traceIdStrategy.createTraceId();
            MdcUtils.put(TraceIdConstant.TRACE_ID, trace);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MdcUtils.clear();
        ThreadLocalUtils.clear();
    }
}
