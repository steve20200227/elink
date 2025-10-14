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

import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;
import cn.snowsoft.iot.module.message.controller.admin.vo.MessageRecordVO;
import cn.snowsoft.iot.module.message.enums.StatusEnum;
import cn.snowsoft.iot.module.message.model.dto.common.SendTaskDto;
import cn.snowsoft.iot.module.message.model.entity.MessageRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


import java.time.LocalDateTime;
import java.util.List;

/**
 * 针对表【message_record(消息记录)】的数据库操作Service
 *
 * @author oszero
 * @version 1.0.0
 */
public interface MessageRecordService extends IService<MessageRecord> {

    IPage getPageList(MessageRecordVO messageRecordVO);

    /**
     * 获取模版详情
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      大小
     * @return 消息记录
     */
    List<MessageRecord> getTemplateInfo(LocalDateTime startTime, LocalDateTime endTime, Integer size);

    /**
     * 获取 APP 详情
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      大小
     * @return 消息记录
     */
    List<MessageRecord> getAppInfo(LocalDateTime startTime, LocalDateTime endTime, Integer size);

    /**
     * 获取推送用户详情
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      大小
     * @return 消息记录
     */
    List<MessageRecord> getPushUserInfo(LocalDateTime startTime, LocalDateTime endTime, Integer size);

    /**
     * 保存消息记录
     *
     * @param sendTaskDto   发送任务
     * @param messageStatus 消息状态
     * @param user          用户
     */
    void saveMessageRecord(SendTaskDto sendTaskDto, StatusEnum messageStatus, String user);


    Integer getRecordCount();
    //根据条件查询消息记录集合
    List getList(MessageRecordVO messageRecordVO);

    //根据条件查询消息记录详情
    List<MessageRecordRespDto> getDetail(MessageRecordVO messageRecordVO);
}
