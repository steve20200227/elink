package cn.snowsoft.iot.module.cps.emqx.properties;

import lombok.Data;

@Data
public class Action {
    // 动作名称
    private String name;
    //动作类型
    private String type;
    //主题
    private String topic;
    // 消息模板
    private String payload;
    // 连接器名称
    private String connector;
}
