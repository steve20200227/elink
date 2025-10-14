package cn.snowsoft.iot.module.cps.controller.admin.equipment.vo;

import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import lombok.Data;

import java.util.Date;

@Data
public class EquipmentPage extends EquipmentDO {

    /**
     * 关联告警配置数
     */
    private Integer warningCount;

    /**
     * 关联点位数
     */
    private Integer pointCount;

    /**
     * 认证器状态：0：新增，1：更新
     */
    private Integer authenticationStatus;

    private String oldUserAccount;

    private String oldUserPassword;

    private Date lastActivated;
}
