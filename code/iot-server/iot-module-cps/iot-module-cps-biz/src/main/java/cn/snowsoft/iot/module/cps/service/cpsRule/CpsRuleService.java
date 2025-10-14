package cn.snowsoft.iot.module.cps.service.cpsRule;

import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.CpsRulePageVO;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.CpsRuleSaveVO;
import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.DeviceProductVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.cpsRule.CpsRule;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

public interface CpsRuleService extends IService<CpsRule> {
    //删除规则
    Boolean mqttDeleteRules(String rulesCode);

    IPage<CpsRulePageVO> selectcpsRulePage(CpsRulePageVO cpsRulePageVO);
    //启用或者失效规则
    Boolean enabledOrDisabledRules(List<Long> equipmentId, boolean isEnabled) throws IOException;

    void saveOrUpdateRule(DeviceProductVO deviceProductVO);
}