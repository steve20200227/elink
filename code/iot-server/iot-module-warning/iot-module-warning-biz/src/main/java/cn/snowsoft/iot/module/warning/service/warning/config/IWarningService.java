package cn.snowsoft.iot.module.warning.service.warning.config;

import cn.snowsoft.iot.framework.common.pojo.CommonResult;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.EquipmentRelateWarningVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo.WarningConfigStatusVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningDetailVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningPageVO;
import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningSaveVO;
import cn.snowsoft.iot.module.warning.dal.dataobject.warning.config.WarningDO;
import cn.snowsoft.iot.module.warning.enums.api.config.vo.ConfigCountVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IWarningService extends IService<WarningDO> {
    IPage<WarningDO> selectWarningPage(WarningPageVO warningPageVO);

    CommonResult<WarningDO> saveWarningConfig(WarningSaveVO warningDO);

    Boolean deleteWarningConfig(Long id);

    CommonResult<WarningDO> updateStatus(WarningDO warningDO);
    //根据 ruleId查询
    WarningDetailVO selectWarningById(Long id);
    //根据warningid 查询
    WarningDetailVO selectWarningByWarningId(Long id);

    CommonResult<WarningSaveVO> getWarningById(Long id);

    CommonResult<Boolean> restart(Long id);

    WarningDO getByRuleId(Long id);

    List<ConfigCountVO> getWarningCountByEquipmentId(List<String> equipmentCodeList);

    Integer getConfifCount();

    List<EquipmentRelateWarningVO> getEquipmentRelateWarning();

    boolean deleteByEquipmentId(Long id);

    List<WarningConfigStatusVO> getStatusCount();

    Boolean untie(Long id, String equipmentCode);

    CommonResult<Boolean> equipmentUntie(String equipmentCode);
}
