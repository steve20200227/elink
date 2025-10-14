package cn.snowsoft.iot.module.cps.dal.dataobject.home;

import cn.snowsoft.iot.module.cps.controller.admin.home.vo.EquipmentAgreementTypeVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.GatewayRelateEquipmentVO;
import cn.snowsoft.iot.module.cps.controller.admin.home.vo.ProductAssociationEquipmentVO;
import lombok.*;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeDO {

    /**
     * 产品数量
     */
    private Integer productCount;

    /**
     * 产品启用数量
     */
    private Integer productEnabelCount;

    /**
     * 产品禁用数量
     */
    private Integer productDisableCount;

    /**
     * 设备数量
     */
    private Integer equipmentCount;

    /**
     * 设备启用数量
     */
    private Integer equipmentEnableCount;

    /**
     * 设备禁用数量
     */
    private Integer equipmentDisableCount;

    /**
     * 驱动数量
     */
    private Integer driveCount;

    /**
     * 驱动启用数量
     */
    private Integer driveEnableCount;

    /**
     * 驱动禁用数量
     */
    private Integer driveDisableCount;

    /**
     * 通道数量
     */
    private Integer passageCount;

    /**
     * 设备协议类型
     */
    private List<EquipmentAgreementTypeVO> equipmentType;

    /**
     * 产品关联设备数
     */
    private List<ProductAssociationEquipmentVO> productREquipmentCount;

    /**
     * 驱动关联设备数据
     */
    private List<GatewayRelateEquipmentVO> gatewayRelateEquipment;

}
