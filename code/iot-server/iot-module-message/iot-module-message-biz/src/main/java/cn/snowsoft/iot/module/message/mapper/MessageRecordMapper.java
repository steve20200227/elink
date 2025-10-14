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

package cn.snowsoft.iot.module.message.mapper;

import cn.snowsoft.iot.framework.common.pojo.PageResult;
import cn.snowsoft.iot.framework.mybatis.core.mapper.BaseMapperX;
import cn.snowsoft.iot.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.snowsoft.iot.module.message.api.messageRecord.dto.MessageRecordRespDto;
import cn.snowsoft.iot.module.message.controller.admin.vo.MessageRecordVO;
import cn.snowsoft.iot.module.message.model.entity.MessageRecord;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 针对表【message_record(消息记录)】的数据库操作Mapper
 *
 * @author oszero
 * @version 1.0.0
 */

public interface MessageRecordMapper extends BaseMapperX<MessageRecord> {
    default PageResult<MessageRecord> getPageList(MessageRecordVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MessageRecord>()
                .eqIfPresent(MessageRecord::getTraceId, reqVO.getTraceId())
                .eqIfPresent(MessageRecord::getTemplateId, reqVO.getTemplateId())
                .eqIfPresent(MessageRecord::getAppId, reqVO.getAppId())
                .eqIfPresent(MessageRecord::getMessageStatus, reqVO.getMessageStatus())
                .eqIfPresent(MessageRecord::getUserType, reqVO.getUserType())
                .eqIfPresent(MessageRecord::getPushUser, reqVO.getPushUser())
                .eqIfPresent(MessageRecord::getChannelType, reqVO.getChannelType())
                .eqIfPresent(MessageRecord::getMessageType, reqVO.getMessageType())
                .eqIfPresent(MessageRecord::getPushRange, reqVO.getPushRange())
                .eqIfPresent(MessageRecord::getRetried, reqVO.getRetried())
                .betweenIfPresent(MessageRecord::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MessageRecord::getCreateTime));
    }


    /**
     * 自定义分页
     *
     * @param page   分页
     * @param
     * @return IPage<Gateway>
     */
    IPage selectPageMessageRecord(IPage page, @Param("messageRecord") MessageRecordVO messageRecord);
    List<MessageRecordRespDto> selectMessageRecord(@Param("messageRecord") MessageRecordVO messageRecord);


    /**
     * 获取指定时间内的 TOP size
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      大小
     * @return 消息记录
     */
    @Select(
            "select mt.template_name as template_name, count(mr.template_id) as value from message_record mr left JOIN message_template mt on mr.template_id = mt.template_id where mr.create_time >= #{startTime} and mr.create_time <= #{endTime} group by mr.template_id order by value desc limit 0, #{size};"
    )
    List<MessageRecord> getTemplateInfo(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("size") Integer size);

    /**
     * 获取指定时间内的 TOP size
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      大小
     * @return 消息记录
     */
    @Select(
            "select ma.app_name as channel_name, count(mr.app_id) as value from message_record mr LEFT JOIN message_app ma on ma.app_id = mr.app_id where mr.create_time >= #{startTime} and mr.create_time <= #{endTime} group by mr.app_id order by value desc limit 0, #{size};"
    )
    List<MessageRecord> getAppInfo(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("size")  Integer size);

    /**
     * 获取指定时间内的 TOP size
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      大小
     * @return 消息记录
     */
    @Select(
            "select MAX(nick_name) as push_user, count(*) as value from message_record where create_time >= #{startTime} and create_time <= #{endTime} group by push_user order by value desc limit 0, #{size};"
    )
    List<MessageRecord> getPushUserInfo(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("size")  Integer size);

    @Select("select count(*) from message_record")
    Integer getRecordCount();
}




