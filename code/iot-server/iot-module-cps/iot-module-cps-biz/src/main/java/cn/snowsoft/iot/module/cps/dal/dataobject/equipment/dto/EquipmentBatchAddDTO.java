package cn.snowsoft.iot.module.cps.dal.dataobject.equipment.dto;

import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentDO;
import lombok.Data;

@Data
public class EquipmentBatchAddDTO extends EquipmentDO {

    /**
     * 新增数量
     */
    private Integer equipmentNum;
    /**
     * 编码前缀
     */
    private String equipmentCodePrefixes;
    /**
     * 编码后缀
     */
    private String equipmentCodeSuffix;
    /**
     * 名称前缀
     */
    private String equipmentNamePrefixes;
    /**
     * 名称后缀
     */
    private String equipmentNameSuffix;

}
