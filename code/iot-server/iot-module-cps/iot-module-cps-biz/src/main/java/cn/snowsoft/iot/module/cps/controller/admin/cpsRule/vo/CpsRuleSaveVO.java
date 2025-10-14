package cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo;

import cn.snowsoft.iot.module.cps.dal.dataobject.cpsRule.CpsRule;
import lombok.Data;

import java.util.List;

@Data
public class CpsRuleSaveVO extends CpsRule {
    //规则数据
   private List<CpsDataEntryVO> cpsDataEntries;
   //动作输出
   private List<CpsActionOutputVO> cpsActionOutputs;
}