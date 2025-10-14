package cn.snowsoft.iot.module.cps.initServer.ekuiper;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ekuiper的配置信息类
 */
@Component
@Data
@ConfigurationProperties(prefix = "ekuiper.config")
@DependsOn("springContextConfig")
public class EkuiperProperties {
    // ekuiper服务URL
    @Value("${ekuiper.url}")
    private String ekuiperUrl;
    @Value("${ekuiper.deviceStatusSourceConfkey:deviceStatusSource}")
    private String configKey;
    // 三个连接器 属性连接器，事件连接器，计算结果连接器
    private List<String> connectionNames;
    // 创建连接配置文件信息URL
    private String connectionsUrl;
    // 创建源配置文件信息URL
    private String sourcesUrl;
    // rabbitmq的mqtt协议通讯地址
    private String server;
    // rabbitmq的账号密码
    private String username;
    private String password;
    // mqtt协议版本
    private String protocolVersion;
    // 是否跳过证书验证
    private boolean insecureSkipVerify;
    // 源配置列表
    private List<Source> sources;

    // 源配置文件信息类
    @Data
    public static class Source {
        // 使用的连接器名称
        private String connectionSelector;
        // 源配置名称
        private String name;
        // 当前源的消息质量等级
        private int qos;
    }
}
