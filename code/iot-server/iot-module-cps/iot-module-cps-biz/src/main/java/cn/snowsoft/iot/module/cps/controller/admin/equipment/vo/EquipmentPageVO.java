package cn.snowsoft.iot.module.cps.controller.admin.equipment.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import lombok.*;


@TableName(value = "cps_equipment", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentPageVO extends PageParam {


    private static final long serialVersionUID = 1L;

    /**
     * 设备编码
     */
    private String equipmentCode;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备类型：直连设备；子设备；网关设备；
     */
    private String equipmentType;

    /**
     * 采集设备Id
     */
    @JsonSerialize(nullsUsing = NullSerializer.class)
    private Long collectionId;

    /**
     * 采集设备编码
     */
    @JsonSerialize(nullsUsing = NullSerializer.class)
    private String collectionCode;

    /**
     * 舱室编码
     */
    private String cabinCode;

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
    @TableField(value = "node_code")
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
     * 通道ID
     */
    private Long passageId;

    /**
     * 点位类型
     */
    private String pointType;
}
