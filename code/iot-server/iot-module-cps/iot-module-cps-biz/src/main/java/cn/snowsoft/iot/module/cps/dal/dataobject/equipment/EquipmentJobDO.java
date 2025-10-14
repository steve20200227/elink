package cn.snowsoft.iot.module.cps.dal.dataobject.equipment;

import cn.snowsoft.iot.framework.mybatis.core.entity.TenantEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import lombok.*;

@TableName(value = "cps_equipment_job", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentJobDO  extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private String jobId;

    /**
     * 执行参数
     */
    private String jobParam;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备编码
     */
    private Long equipmentId;

    /**
     * 任务状态
     */
    private Integer jobStatus;

    /**
     * 定时规则
     */
    private String jobCron;


}
