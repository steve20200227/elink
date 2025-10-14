package cn.snowsoft.iot.module.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Schema(description = "业务调用接收参数实体类")
@Data
public class XxlJobEntity {

    @Schema(description = "任务id")
    private int id;

    @Schema(description = "任务描述")
    private String jobDesc;

    @Schema(description = "定时规则")
    private String cron;

    @Schema(description = "执行参数")
    private String jobParam;

    @Schema(description = "执行器组名称")
    private String appName;

    @Schema(description = "执行器")
    private String executorHandler;

    @Schema(description = "类型: 设备或场景")
    private String jobType;

    @Schema(description = "关联id: 设备id或场景记录id")
    private Long relevanceId;
    @Schema(description = "重试次数")
    private int executorFailRetryCount;		// 失败重试次数

}
