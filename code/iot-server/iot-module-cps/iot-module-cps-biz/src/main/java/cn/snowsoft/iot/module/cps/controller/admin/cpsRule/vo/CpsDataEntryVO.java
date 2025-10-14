package cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo;

import lombok.Data;

/**
 * 规则数据输入
 *
 * @author Chill
 */
@Data
public class CpsDataEntryVO {
    /**
     * id
     */
    private String id;
    /**
     * 输入主题
     */
    private String inputTheme;
    /**
     * 输入类型
     */
    private String inputType;
}