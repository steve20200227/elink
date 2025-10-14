package cn.snowsoft.iot.module.job.controller.vo;

import lombok.Data;

@Data
public class JobLogPageVO {

    private Integer PageNo = 1;

    private Integer pageSize = 10;

    // 任务组Id
    private Integer jobGroup = 0;

    // 任务Id
    private Integer jobId = 0;

    // 日志状态
    private Integer alarmStatus = 0;

    // 调度时间
    private String filterTime;

    // 处理器名称
    private String executorHandler;

}
