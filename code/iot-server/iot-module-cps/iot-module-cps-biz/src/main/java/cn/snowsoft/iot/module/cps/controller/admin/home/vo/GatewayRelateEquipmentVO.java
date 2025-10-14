package cn.snowsoft.iot.module.cps.controller.admin.home.vo;

import lombok.Data;

@Data
public class GatewayRelateEquipmentVO {

    /**
     * 驱动id
     */
    private Long id;

    /**
     * 驱动名称
     */
    private String gatewayName;

    /**
     * 关联设备数
     */
    private Integer equipmentCount = 0;

}
