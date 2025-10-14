package cn.snowsoft.iot.module.cps.controller.admin.equipment.vo;

import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import lombok.Data;

import java.util.Date;

@Data
public class EquipmentVO extends EquipmentDO {

    /**
     * 父设备
     */
    private EquipmentDO parentEquipmentDO;
    /**
     * 上报id
     */
    private Long reportId;
    /**
     * 下发id
     */
    private Long deliverAnId;

    //事件上报id
    private Long eventId;

    private Date lastActivated;
}
