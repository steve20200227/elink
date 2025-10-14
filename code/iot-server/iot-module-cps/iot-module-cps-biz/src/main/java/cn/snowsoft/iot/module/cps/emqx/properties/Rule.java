package cn.snowsoft.iot.module.cps.emqx.properties;

import lombok.Data;

import java.util.List;
@Data
public class Rule {
    private String id;
    // 规则名称
    private String name;
    // 规则sql
    private String sql;
    // 规则配置动作名称列表
    private List<String> actions;
}
