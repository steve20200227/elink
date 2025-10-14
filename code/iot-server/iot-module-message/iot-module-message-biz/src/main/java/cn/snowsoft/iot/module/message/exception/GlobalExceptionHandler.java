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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.message.enums.ResultEnum;
import cn.snowsoft.iot.module.message.enums.StatusEnum;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import cn.snowsoft.iot.module.message.util.MessageLinkTraceUtils;
import cn.snowsoft.iot.module.message.util.ThreadLocalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 全局异常处理器
 *
 * @author oszero
 * @version 1.0.0
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageRecordService messageRecordService;

    /**
     * DTO 参数校验失败的异常处理
     *
     * @param e       异常信息
     * @param request 请求
     * @return CommonResult
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<Void> handleBindingException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("请求参数非预期异常: {} - {}, error = {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        FieldError firstFieldError = CollectionUtil.getFirst(bindingResult.getFieldErrors());
        String exceptionStr = Optional.ofNullable(firstFieldError)
                .map(FieldError::getDefaultMessage)
                .orElse(StrUtil.EMPTY);
        return CommonResult.error(500,exceptionStr);
    }

    /**
     * 消息链路异常时的错误处理
     *
     * @param e       异常信息
     * @param request 请求
     * @return CommonResult
     */
    @ExceptionHandler(MessageException.class)
    public CommonResult<?> handleMessageException(MessageException e, HttpServletRequest request) {
        // 记录错误日志
        MessageLinkTraceUtils.recordMessageLifecycleErrorLog(e.getMessage());
        // 记录异常信息到生命周期日志中
        MessageLinkTraceUtils.recordMessageLifecycleError2InfoLog(e.getMessage());
        try {
            // 记录消息发送失败
            SendTaskDto sendTaskDto = ThreadLocalUtils.getSendTaskDto();
            sendTaskDto.getUsers().forEach(user -> messageRecordService.saveMessageRecord(sendTaskDto, StatusEnum.OFF, user));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return CommonResult.error(500,e.getMessage());
    }

    /**
     * 系统异常时的错误处理
     *
     * @param e       异常信息
     * @param request 请求
     * @return CommonResult
     */
    @ExceptionHandler(SystemException.class)
    public CommonResult<?> handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("[SystemException], {}", e.getMessage());
        return CommonResult.error(500,e.getMessage());
    }

    /**
     * 异常兜底处理
     *
     * @param e       异常信息
     * @param request 请求
     * @return CommonResult
     */
    @ExceptionHandler(Throwable.class)
    public CommonResult<?> handleThrowable(Exception e, HttpServletRequest request) {
        log.error("[handleThrowable], {} ", e.getMessage());
        return CommonResult.error(500,"");
    }


}
