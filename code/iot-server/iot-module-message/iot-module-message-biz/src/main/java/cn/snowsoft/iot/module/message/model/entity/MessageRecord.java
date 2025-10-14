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

package cn.snowsoft.iot.module.message.model.entity;

import cn.snowsoft.iot.framework.mybatis.core.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息记录
 *
 * @author oszero
 * @version 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "message_record")
public class MessageRecord implements Serializable {
    /**
     * 消息链路 id
     */
    private String traceId;

    /**
     * 模板 id
     */
    private Long templateId;

    /**
     * appId
     */
    private Long appId;

    /**
     * 消息状态（1-发送成功0-发送失败）
     */
    private Integer messageStatus;

    /**
     * 用户类型（1-企业账号2-手机3-邮箱4-平台userId）
     */
    private Integer userType;

    /**
     * 消息推送人
     */
    private String pushUser;

    /**
     * 发送范围
     */
    private Integer pushRange;

    /**
     * 发送渠道类型
     */
    private Integer channelType;

    /**
     * 消息类型（见 MessageTypeEnum）
     */
    private String messageType;

    /**
     * 是否重试消息（1-是 0-首次发送）
     */
    private Integer retried;

    /**
     * 创建时间
     */
    private Date createTime;


    private String warningRecordId;

    private Long warningActionId;

    private String failReason;

    @TableField(exist = false)
    private Long value;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    private String templateName;

    private String channelName;

    private String nickName;
}