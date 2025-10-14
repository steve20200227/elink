package cn.snowsoft.iot.module.cps.emqx.properties;

import lombok.Data;

@Data
public class Connectors {
    // 连接器名称
    private String name;
    // 连接器类型
    private String type;
    // mqtt服务地址
    private String server;
    // 用户名
    private String username;
    // 密码
    private String password;
}
