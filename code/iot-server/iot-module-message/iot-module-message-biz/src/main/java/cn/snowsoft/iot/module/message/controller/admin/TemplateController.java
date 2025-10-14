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
import cn.snowsoft.iot.module.message.model.dto.request.*;
import cn.snowsoft.iot.module.message.model.dto.response.MessageTypeResponseDto;
import cn.snowsoft.iot.module.message.model.dto.response.TemplateSearchResponseDto;
import cn.snowsoft.iot.module.message.model.entity.Template;
import cn.snowsoft.iot.module.message.service.TemplateService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 模板控制器
 *
 * @author oszero
 * @version 1.0.0
 */
@Validated
@RestController
@RequestMapping("/message/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    /**
     * 分页搜索模版
     *
     * @param templateSearchRequestDto 查询参数
     * @return Page
     */
    @PostMapping("/search")
    public CommonResult<Page<TemplateSearchResponseDto>> search(@RequestBody TemplateSearchRequestDto templateSearchRequestDto) {
        Page<TemplateSearchResponseDto> page = templateService.search(templateSearchRequestDto);
        return CommonResult.success(page);
    }

    /**
     * 查询模版列表
     *
     * @return List
     */
    @GetMapping("/getTemplateList")
    public CommonResult<List<TemplateSearchResponseDto>> getTemplateList(@RequestParam("status") int status) {
        List<TemplateSearchResponseDto> list = templateService.getTemplateList(status);
        return CommonResult.success(list);
    }

    /**
     * 保存模版
     *
     * @param dto 参数
     * @return 成功
     */
    @PostMapping("/saveTemplate")
    public CommonResult<?> saveTemplate(@Valid @RequestBody TemplateSaveAndUpdateRequestDto dto) {
        templateService.save(dto);
        return CommonResult.success("");
    }

    /**
     * 更新通过 ID
     *
     * @param dto 参数
     * @return 成功
     */
    @PostMapping("/updateById")
    public CommonResult<?> updateById(@Valid @RequestBody TemplateSaveAndUpdateRequestDto dto) {
        templateService.updateById(dto);
        return CommonResult.success("");
    }

    /**
     * 更新模版状态通过 ID
     *
     * @param dto 参数
     * @return 成功
     */
    @PostMapping("/updateStatusById")
    public CommonResult<?> updateStatusById(@Valid @RequestBody TemplateUpdateStatusRequestDto dto) {
        templateService.updateStatusById(dto);
        return CommonResult.success("");
    }

    /**
     * 批量删除
     *
     * @param dto 参数
     * @return 成功
     */
    @PostMapping("/deleteByIds")
    public CommonResult<?> deleteByIds(@Valid @RequestBody DeleteIdsRequestDto dto) {
        templateService.deleteByIds(dto);
        return CommonResult.success("");
    }

    /**
     * 获取消息类型通过渠道类型
     *
     * @param dto dto
     * @return 消息类型
     */
    @PostMapping("/getMessageTypeByChannelType")
    public CommonResult<List<MessageTypeResponseDto>> getMessageTypeByChannelType(@Valid @RequestBody TemplateAddGetByChannelRequestDto dto) {
        List<MessageTypeResponseDto> messageTypeResponseDtoList = templateService.getMessageTypeByChannelType(dto);
        return CommonResult.success(messageTypeResponseDtoList);
    }

    /**
     * 测试消息发送功能
     *
     * @param sendTestRequestDto 参数
     * @return 成功
     */
    @PostMapping("/testSendMessage")
    public CommonResult<?> testSendMessage(@Valid @RequestBody SendTestRequestDto sendTestRequestDto, HttpServletRequest request) {
        templateService.testSendMessage(sendTestRequestDto,request);
        return CommonResult.success("");
    }

    /**
     * 获取消息参数通过消息类型
     *
     * @param dto TemplateMessageParamByMessageTypeRequestDto
     * @return 消息参数
     */
    @PostMapping("/getMessageParamByMessageType")
    public CommonResult<String> getMessageParamByMessageType(@Valid @RequestBody TemplateMessageParamByMessageTypeRequestDto dto) {
        return CommonResult.success(templateService.getMessageParamByMessageType(dto));
    }

    /**
     * excel 批量导入模板
     *
     * @param multipartFile 文件对象
     * @return 通用响应
     */
    @PostMapping("/fileBatchSave")
    public CommonResult<?> fileBatchSave(@RequestParam("excel") MultipartFile multipartFile) {

        return CommonResult.success("");
    }

    /**
     * 根据ids获取对应数据
     */
    @PostMapping("/getByIds")
    public CommonResult<List<TemplateSearchResponseDto>> getByIds(@RequestParam("ids") String ids) {
        return CommonResult.success(templateService.getByIds(ids));
    }

    /**
     * 根据id获取消息末班详细数据
     */
    @PostMapping("/getById")
    public CommonResult<TemplateSearchResponseDto> getById(@RequestParam("id") String id) {
        return CommonResult.success(templateService.getTemplateById(id));
    }
}
