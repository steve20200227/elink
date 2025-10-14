package cn.snowsoft.iot.module.cps.api.equipment.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - Admin 设备 EquipmentDTO DTO")
@Data
public class EquipmentDTO {

    /**
     * 设备 id
     */
    private Long id;

    /**
     * 设备编码
     */
    private String equipmentCode;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品编码
     */
    private String productCode;

    /**
     * 是否启用
     */
    private Integer isEnable;

    /**
     * 备注
     */
    private String remark;

    /**
     * 节点编号
     */
    private String nodeCode;

    /**
     * 协议类型
     */
    private String agreementType;

    /**
     * 设备所属
     */
    private String equipmentAscription;
    /**
     * 属性上报
     */
    private String attributePush;

    /**
     * 事件上报
     */
    private String eventPush;

    /**
     * 功能下推
     */
    private String featureIssued;
}
