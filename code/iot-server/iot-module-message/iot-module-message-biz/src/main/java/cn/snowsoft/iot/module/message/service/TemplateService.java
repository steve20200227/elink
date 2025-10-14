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

package cn.snowsoft.iot.module.message.service;

import cn.snowsoft.iot.module.message.model.dto.request.*;
import cn.snowsoft.iot.module.message.model.dto.response.MessageTypeResponseDto;
import cn.snowsoft.iot.module.message.model.dto.response.TemplateSearchResponseDto;
import cn.snowsoft.iot.module.message.model.entity.Template;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 模板 service
 *
 * @author oszero
 * @version 1.0.0
 */
public interface TemplateService extends IService<Template> {

    /**
     * 分页搜索模版
     *
     * @param templateSearchRequestDto 参数
     * @return Page
     */
    Page<TemplateSearchResponseDto> search(TemplateSearchRequestDto templateSearchRequestDto);

    List<TemplateSearchResponseDto> getTemplateList(int status);

    /**
     * 批量删除
     *
     * @param dto 参数
     */
    void deleteByIds(DeleteIdsRequestDto dto);

    /**
     * 更新通过 ID
     *
     * @param dto 参数
     */
    void updateById(TemplateSaveAndUpdateRequestDto dto);

    /**
     * 更新状态
     *
     * @param dto 参数
     */
    void updateStatusById(TemplateUpdateStatusRequestDto dto);

    /**
     * 保存
     *
     * @param dto 参数
     */
    void save(TemplateSaveAndUpdateRequestDto dto);

    /**
     * 获取消息类型通过渠道
     *
     * @param dto 参数
     * @return 消息
     */
    List<MessageTypeResponseDto> getMessageTypeByChannelType(TemplateAddGetByChannelRequestDto dto);

    /**
     * 测试发送消息
     *
     * @param sendTestRequestDto 参数
     */
    void testSendMessage(SendTestRequestDto sendTestRequestDto, HttpServletRequest request);

    /**
     * 获取消息参数模版通过消息类型
     *
     * @param dto dto
     * @return 消息参数
     */
    String getMessageParamByMessageType(TemplateMessageParamByMessageTypeRequestDto dto);

    List<TemplateSearchResponseDto> getByIds(String ids);

    TemplateSearchResponseDto getTemplateById(String id);
}
