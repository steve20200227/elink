package cn.snowsoft.iot.module.warning.dal.dataobject.warning.config;

import cn.snowsoft.iot.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 告警实体类
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warning_config")
public class WarningDO extends BaseDO {

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 场景名称
     */
    private String warningName;

    /**
     * 场景类型
     */
    private String warningType;

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 场景类型
     */
    private String whereType;

    /**
     * 执行动作收敛类型
     */
    private String actionProcessType;

    /**
     * 执行动作收敛值, 多少次
     */
    private int actionProcessValue;

    /**
     * 动作收敛
     */
    private String actionTimeType;

    /**
     * 动作收敛，时间值
     */
    private int actionTimeValue;

    /**
     * 条件json
     */
    private String ruleExpression;

    /**
     * 场景需要生成的规则或内存源json信息，在场景启动时解析并调用计算引擎进行生成
     */
    private String ruleSql;

    /**
     * 规则id（发送到ekuiper上）
     */
    private String ruleId;

    /**
     * 时间窗口大小
     */
    private int timeRange;

    /**
     * 时间窗口单位类型
     */
    private String timeType;

    /**
     * 适用设备编码集合
     */
    private String equipmentCode;

    /**
     * 生效时间段- 开始
     */
    @JsonFormat(pattern = "HH:mm:ss")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime beginTime;

    /**
     * 生效时间段- 结束
     */
    @JsonFormat(pattern = "HH:mm:ss")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    /**
     * 定时类型场景的规则定时表达式
     */
    private String cron;

    /**
     * 定时场景的xxljob任务id
     */
    private int jobId;

    //重试次数
    private int executorFailRetryCount;
}