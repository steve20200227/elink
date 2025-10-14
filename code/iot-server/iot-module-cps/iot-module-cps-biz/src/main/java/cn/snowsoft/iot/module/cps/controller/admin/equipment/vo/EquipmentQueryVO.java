package cn.snowsoft.iot.module.cps.controller.admin.equipment.vo;

import lombok.Data;

import java.util.List;

@Data
public class EquipmentQueryVO {

    // 点位编码
    private String featureCode;

    private List<Long> equipmentIds;

}
