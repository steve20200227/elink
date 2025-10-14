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

package cn.snowsoft.iot.module.message.service.impl;

import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;

import cn.snowsoft.iot.module.message.controller.admin.vo.MessageRecordVO;
import cn.snowsoft.iot.module.message.enums.StatusEnum;
import cn.snowsoft.iot.module.message.exception.BusinessException;
import cn.snowsoft.iot.module.message.mapper.MessageRecordMapper;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.model.entity.MessageRecord;
import cn.snowsoft.iot.module.message.service.MessageRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author sft
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class MessageRecordServiceImpl extends ServiceImpl<MessageRecordMapper, MessageRecord>
        implements MessageRecordService {

    private final MessageRecordMapper messageRecordMapper;

    @Override
    public IPage getPageList(MessageRecordVO messageRecordVO) {
        Page page = new Page(messageRecordVO.getPageNo(),messageRecordVO.getPageSize());
        IPage pageList = messageRecordMapper.selectPageMessageRecord(page,messageRecordVO);
        return pageList;
    }

    @Override
    public List<MessageRecord> getTemplateInfo(LocalDateTime startTime, LocalDateTime endTime, Integer size) {
        return messageRecordMapper.getTemplateInfo(startTime, endTime, size);
    }

    @Override
    public List<MessageRecord> getAppInfo(LocalDateTime startTime, LocalDateTime endTime, Integer size) {
        return messageRecordMapper.getAppInfo(startTime, endTime, size);
    }

    @Override
    public List<MessageRecord> getPushUserInfo(LocalDateTime startTime, LocalDateTime endTime, Integer size) {
        return messageRecordMapper.getPushUserInfo(startTime, endTime, size);
    }

    @Override
    public void saveMessageRecord(SendTaskDto sendTaskDto, StatusEnum messageStatus, String user) {

        MessageRecord messageRecord = MessageRecord.builder()
                .traceId(sendTaskDto.getTraceId())
                .templateId(sendTaskDto.getTemplateId())
                .appId(sendTaskDto.getAppId())
                .channelType(sendTaskDto.getChannelType())
                .messageType(sendTaskDto.getMessageType())
                .userType(sendTaskDto.getUsersType())
                .pushUser(user)
                .pushRange(sendTaskDto.getPushRange())
                .retried(sendTaskDto.getRetried())
                .messageStatus(messageStatus.getStatus())
                .warningRecordId(sendTaskDto.getWarningRecordId())
                .warningActionId(sendTaskDto.getWarningActionId())
                .failReason(sendTaskDto.getFaliReason())
                .nickName(sendTaskDto.getNickName())
                .build();
        this.save(messageRecord);
    }

    @Override
    public List getList(MessageRecordVO messageRecordVO) {
        if (StringUtils.isEmpty(messageRecordVO.getWarningRecordId())){
            throw new BusinessException("告警记录唯一标识不能为空");
        }
        if (messageRecordVO.getWarningActionId() != null){
            throw new BusinessException("动作id唯一标识不能为空");
        }
        LambdaQueryWrapper<MessageRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MessageRecord::getWarningRecordId, messageRecordVO.getWarningRecordId());
        lambdaQueryWrapper.eq(MessageRecord::getWarningActionId, messageRecordVO.getWarningActionId());

        List<MessageRecord> messageRecords = messageRecordMapper.selectList(lambdaQueryWrapper);

        return messageRecords;
    }

    @Override
    public List<MessageRecordRespDto> getDetail(MessageRecordVO messageRecordVO) {
        List<MessageRecordRespDto> messageRecordRespVO = messageRecordMapper.selectMessageRecord(messageRecordVO);

        return messageRecordRespVO;
    }

    @Override
    public Integer getRecordCount() {
        return baseMapper.getRecordCount();
    }
}




