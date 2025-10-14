package cn.snowsoft.iot.module.cps.controller.admin.home.vo;

import lombok.Data;

@Data
public class PassageRelateEquipmentVO {

    /**
     * 通道id
     */
    private Long id;

    /**
     * 通道名称
     */
    private String passageName;

    /**
     * 驱动id
     */
    private Long driveId;

    /**
     * 关联设备数
     */
    private Integer equipmentCount;


}
