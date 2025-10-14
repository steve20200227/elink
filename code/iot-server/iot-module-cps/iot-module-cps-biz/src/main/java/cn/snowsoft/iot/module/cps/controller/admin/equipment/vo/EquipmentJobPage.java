package cn.snowsoft.iot.module.cps.controller.admin.equipment.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import lombok.Data;

@Data
public class EquipmentJobPage extends PageParam {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编码
     */
    private Long equipmentId;
    /**
     * 设备编码
     */
    private String jobName;

}
