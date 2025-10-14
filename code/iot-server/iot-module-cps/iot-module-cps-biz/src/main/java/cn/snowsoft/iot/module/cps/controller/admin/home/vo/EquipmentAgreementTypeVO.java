package cn.snowsoft.iot.module.cps.controller.admin.home.vo;

import lombok.Data;

@Data
public class EquipmentAgreementTypeVO {

    /**
     * 协议类型名称
     */
    private String typeName;

    /**
     * 设备数量
     */
    private Integer equipmentCount;
}
