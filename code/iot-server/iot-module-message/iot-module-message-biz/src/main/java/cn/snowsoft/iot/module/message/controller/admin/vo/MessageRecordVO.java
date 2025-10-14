package cn.snowsoft.iot.module.message.controller.admin.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.snowsoft.iot.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 消息记录分页 Request VO")
@Data
@ToString(callSuper = true)
public class MessageRecordVO extends PageParam {
    @Schema(description = "消息链路 id", example = "30211")
    private String traceId;

    @Schema(description = "模板 id", example = "17399")
    private Long templateId;

    @Schema(description = "appId", example = "13028")
    private Long appId;

    @Schema(description = "消息状态（1-发送成功0-发送失败）", example = "2")
    private Integer messageStatus;

    @Schema(description = "用户类型（1-企业账号2-手机3-邮箱4-平台userId）", example = "1")
    private Integer userType;

    @Schema(description = "消息推送人")
    private String pushUser;

    @Schema(description = "发送渠道类型", example = "1")
    private Integer channelType;

    @Schema(description = "消息类型（见 MessageTypeEnum）", example = "1")
    private String messageType;

    @Schema(description = "推送范围（0-不限1-企业内部2-外部）")
    private Integer pushRange;

    @Schema(description = "是否重试消息（1-是 0-首次发送）")
    private Integer retried;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;


    private String warningRecordId;

    private Long warningActionId;

    private String appName;

    private String templateName;
}
