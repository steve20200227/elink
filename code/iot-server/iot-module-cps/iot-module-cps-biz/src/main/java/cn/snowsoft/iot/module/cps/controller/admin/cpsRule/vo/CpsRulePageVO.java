package cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo;

import cn.snowsoft.iot.framework.common.pojo.PageParam;
import lombok.Data;

@Data
public class CpsRulePageVO extends PageParam {
    private static final long serialVersionUID = 1L;

    /**
     * 规则编码同步mqtt  id
     */
    private String ruleCode;

    /**
     * 是否启用
     */
    private String isEnable;
    /**
     * 备注
     */
    private String remark;
    /**
     * sql
     */
    private String sql;
    /**
     * 1.产品2.设备3.规则
     */
    private String ruleType;
    
}