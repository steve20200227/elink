package cn.snowsoft.iot.module.cps.api.equipment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "RPC 服务 - Admin 设备 EquipmentRelateWarningDTO DTO")
@Data
public class EquipmentRelateWarningDTO implements Serializable {

    /**
     * 设备id
     */
    private Long equipmentId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 告警数
     */
    private Integer warningCount;

}
