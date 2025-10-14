package cn.snowsoft.iot.module.cps.dal.mysql.cpsRule;

import cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo.CpsRulePageVO;
import cn.snowsoft.iot.module.cps.dal.dataobject.cpsRule.CpsRule;
import cn.snowsoft.iot.module.cps.dal.dataobject.equipment.EquipmentPointDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface CpsRuleServiceMapper extends BaseMapper<CpsRule> {
    IPage<CpsRulePageVO> selectCpsRulePage(IPage<EquipmentPointDO> page,@Param("entity") CpsRulePageVO cpsRulePageVO);
}