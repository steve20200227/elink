package cn.snowsoft.iot.module.warning.controller.admin.warning.record.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WarningRecordPageVO extends PageParam {

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 告警配置 id
     */
    private Long configId;

    /**
     * 点位值
     */
    private String pointValue;

    /**
     * 告警配置名称
     */
    private String configName;

    /**
     * 告警数据
     */
    private String warningData;

    /**
     * 设备 id
     */
    private Long equipmentId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 点位 id
     */
    private Long pointId;

    /**
     * 点位名称
     */
    private String pointName;

    /**
     * 告警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime warningTime;

    /**
     * 告警状态
     */
    private Integer status;

    /**
     * 处理用户
     */
    private String handleUser;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理说明
     */
    private String remark;

    private String warningType;

    private String createTimes;
}
