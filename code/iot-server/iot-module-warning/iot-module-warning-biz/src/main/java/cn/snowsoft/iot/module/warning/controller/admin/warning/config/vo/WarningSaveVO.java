package cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo;

import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigRule;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.WarningDO;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningSaveVO extends WarningDO {

    /**
     * 适用设备编码集合
     */
    private List<String> equipmentList;

    /**
     * 使用设备名称集合
     */
    private List<EquipmentVO> equipmentName;

    /**
     * 条件集合
     */
    private List<ConfigRule> whereList;

    /**
     * 动作列表
     */
    private List<ConfigActionDO> actionDOList;
}
