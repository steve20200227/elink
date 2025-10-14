package cn.snowsoft.iot.module.warning.controller.admin.warning.home.vo;

import lombok.Data;

@Data
public class RuleUseCountVO {

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 使用次数
     */
    private Integer useCount;

}
