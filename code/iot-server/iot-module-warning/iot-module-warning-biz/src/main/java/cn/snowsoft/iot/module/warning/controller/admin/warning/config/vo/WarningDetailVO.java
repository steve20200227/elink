package cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo;

import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.ConfigActionDO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.WarningDO;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarningDetailVO extends WarningDO {


    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 点位名称
     */
    private String pointName;

    /**
     * 动作列表
     */
    private List<ConfigActionDO> actionDOList;

}
