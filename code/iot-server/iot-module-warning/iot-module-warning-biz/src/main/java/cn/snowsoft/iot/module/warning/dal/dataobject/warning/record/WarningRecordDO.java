package cn.snowsoft.iot.module.warning.dal.dataobject.warning.record;

import cn.snowsoft.iot.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warning_record")
public class WarningRecordDO extends BaseDO {

    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 点位值
     */
    private String pointValue;

    /**
     * 告警配置 id
     */
    private Long configId;

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
     * 设备编码
     */
    private String equipmentCode;

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

    /**
     * 存储uuid，用于在告警记录处查询动作执行记录
     */
    private String warningTag;

    //告警类型  定时  手动  条件
    private String warningType;
}
