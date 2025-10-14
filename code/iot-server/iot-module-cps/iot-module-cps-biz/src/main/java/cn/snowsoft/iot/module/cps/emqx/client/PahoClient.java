package cn.snowsoft.iot.module.cps.emqx.client;

import cn.snowsoft.iot.module.cps.emqx.properties.Client;
import cn.snowsoft.iot.module.cps.emqx.properties.EmqxProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author chen_gang
 * @date 2022/11/14
 * 连接EMQX工具类
 */
@Data
@Component
@Slf4j
@ConditionalOnProperty(prefix = "mqtt.client", name = "enable", havingValue = "true")
public class PahoClient {
    @Autowired
    private EmqxProperties emqxProperties;

    private Client clientProperties;

    private MqttClient client;

    private MqttTopic mqttTopic;

	//连接指定云端服务器
    @PostConstruct
    private void run() {
        clientProperties = emqxProperties.getClient();
        try {
            client = new MqttClient(clientProperties.getServerUrl(), clientProperties.getClientId(), new MemoryPersistence());
        } catch (MqttException e) {
            log.error("创建与服务器连接时发生错误, serverUrl: {}", clientProperties.getServerUrl());
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        // 设置断开链接后自动重连
        options.setAutomaticReconnect(true);
        // 设置超时时间
        options.setConnectionTimeout(60);
        // 设置会话心跳时间
        options.setKeepAliveInterval(60);
		// 设置最大飞行数
		options.setMaxInflight(clientProperties.getMaxInflight());
        // 设置用户名
        options.setUserName(clientProperties.getUserName());
        // 设置密码
        options.setPassword(clientProperties.getPassword().toCharArray());
        try {
            client.setCallback(new PahoCallback(this));
            client.connect(options);

            if (client.isConnected()) {
                log.info("已连接到服务器: {}", clientProperties.getServerUrl());
            } else {
                log.warn("未连接到服务器: {}", clientProperties.getServerUrl());
            }
        } catch (Exception e) {
            log.error("连接服务器错误：", e);
        }
    }

    /**
     * 发布消息
     *
     * @param topic   主题
     * @param message 消息体
     * @return 发送结果
     */
    public boolean publish(String topic, String message) {
        MqttDeliveryToken token = null;
        MqttMessage mqttMessage = null;

        if (isConnected()) {
            mqttMessage = new MqttMessage();
            mqttMessage.setRetained(false);
            mqttMessage.setQos(0);
            mqttTopic = client.getTopic(topic);
            log.info("mqtt 发布主题：{}，发布内容: {}", topic, message);
            try {
                mqttMessage.setPayload(message.getBytes("UTF-8"));
                token = mqttTopic.publish(mqttMessage);
                token.waitForCompletion();
                if (token.isComplete() && token.getException() == null) {
                    log.info("mqtt 消息发布状态: 成功   {},  {}", new Object[]{topic, message});
                    return true;
                } else {
                    log.error("mqtt 消息发布状态: 失败   {},  {}", new Object[]{topic, message});
                    return false;
                }
            } catch (MqttException | UnsupportedEncodingException e) {
                log.error("mqtt 消息发布状态: 失败   {},  {}, {}", new Object[]{topic, message, e});
                return false;
            }
        } else {
            log.warn("消息发布失败，未连接服务器！");
            return false;
        }
    }

    /**
     * 订阅主题
     *
     * @param topics 要订阅的主题数组
     * @return 成功 ：true
     */
    public boolean subscribe(String[] topics) {
        Integer[] qos = Arrays.stream(topics).map(e -> 1).collect(Collectors.toList()).toArray(new Integer[0]);
        if (isConnected()) {
            try {
                client.subscribe(topics, Arrays.stream(qos).mapToInt(Integer::intValue).toArray());
                log.info("主题订阅成功：{}", Arrays.toString(topics));
                return true;
            } catch (MqttException e) {
                log.error("主题订阅失败：{}, {}", Arrays.toString(topics), e);
                return false;
            }
        } else {
            log.warn("订阅失败，未连接服务器！");
            return false;
        }
    }

    /**
     * 关闭与云平台的连接
     *
     * @param timeOut timeOut
     * @throws MqttException MqttException
     */
    public void closeConnect(long timeOut) throws MqttException {
        if (isConnected()) {
            client.disconnect(timeOut);
        }
        log.info("已断开与服务器连接");
    }

    /**
     * 是否已连接云平台
     *
     * @return boolean
     */
    public boolean isConnected() {
        return (client != null && client.isConnected());
    }
}
