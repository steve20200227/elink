package cn.snowsoft.iot.module.cps.controller.admin.home.vo;

import lombok.Data;

@Data
public class ProductAssociationEquipmentVO {

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 设备数量
     */
    private Integer equipmentCount;
}
