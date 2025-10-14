package cn.snowsoft.iot.module.cps.emqx.properties;

import lombok.Data;

/**
 * @author: chen_gang
 * @date: 2025/1/22 15:15
 * @description: mqtt客户端配置类
 */
@Data
public class Client {
    private boolean enable;

    private String clientId;

    private int maxInflight;

    private String userName;

    private String password;

    private String serverUrl;
}
