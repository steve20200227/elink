package cn.snowsoft.iot.module.cps.controller.admin.cpsRule.vo;

import lombok.Data;

/**
 * 动作输出
 */
@Data
public class CpsActionOutputVO {
    /**
     * id
     */
    private String id;
    /**
     * 动作类型
     */
    private String actionType;
    /**
     * 动作主题
     */
    private String actionTheme;
    /**
     * Payload
     */
    private String payload;
}