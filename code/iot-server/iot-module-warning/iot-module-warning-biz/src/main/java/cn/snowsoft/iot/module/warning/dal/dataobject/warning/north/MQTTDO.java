package cn.snowsoft.iot.module.warning.dal.dataobject.warning.north;

import lombok.Data;

@Data
public class MQTTDO {

    /**
     * MQTT服务地址
     */
    private String mqttAddress;

    /**
     * 客户端ID
     */
    private String clsId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * qos
     */
    private Integer qos;

    /**
     * 是否保留
     */
    private String retain;

    /**
     * 主题
     */
    private String theme;
}
