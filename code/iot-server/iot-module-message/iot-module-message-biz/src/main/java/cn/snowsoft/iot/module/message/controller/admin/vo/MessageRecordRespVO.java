package cn.snowsoft.iot.module.message.controller.admin.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MessageRecordRespVO {
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
     * 用户类型
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
     * 是否重试消息
     */
    private Integer retried;

    /**
     * 创建时间
     */
    private Date createTime;


    private String warningRecordId;

    private Long warningActionId;

    private String appName;

    private String templateName;

    private String nickName;
}
