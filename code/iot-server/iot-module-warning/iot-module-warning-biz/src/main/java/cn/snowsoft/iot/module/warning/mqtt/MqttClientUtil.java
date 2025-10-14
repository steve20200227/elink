package cn.snowsoft.iot.module.warning.mqtt;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;

/**
 * @author chen_gang
 * @date 2022/11/14
 * 连接EMQX工具类
 */
@Data
@Slf4j
@RequiredArgsConstructor
public class MqttClientUtil {
    /**
     * 客户端id，未指定则随机生成
     */
    private final String clientId;

    /**
     * 用户名
     */
    private final String userName;

    /**
     * 密码
     */
    private final String password;

    /**
     * mqtt服务地址
     */
    private final String serverUrl;

    private MqttClient client;

    private MqttTopic mqttTopic;

	//连接指定云端服务器
    public boolean connect() {
        try {
            client = new MqttClient(serverUrl, clientId, new MemoryPersistence());
        } catch (MqttException e) {
            log.error("创建与服务器连接时发生错误, serverUrl: {}", serverUrl);
            return false;
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
		options.setMaxInflight(100);
        // 设置用户名
        options.setUserName(userName);
        // 设置密码
        options.setPassword(password.toCharArray());
        try {
            client.setCallback(new MqttClientCallback(this));
            //建立连接
            client.connect(options);

            if (client.isConnected()) {
                log.info("已连接到服务器: {}", serverUrl);
                return true;
            } else {
                log.warn("未连接到服务器: {}", serverUrl);
                return false;
            }
        } catch (Exception e) {
            log.error("连接服务器错误：", e);
            return false;
        }
    }

    /**
     * 发布消息
     *
     * @param topic   主题
     * @param message 消息体
     * @return 发送结果
     */
    public boolean publish(String topic, String message, boolean retained, int qos) {
        MqttDeliveryToken token = null;
        MqttMessage mqttMessage = null;

        if (isConnected()) {
            mqttMessage = new MqttMessage();
            mqttMessage.setRetained(retained);
            mqttMessage.setQos(qos);
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
     * 关闭与云平台的连接
     *
     * @param timeOut timeOut
     * @throws MqttException MqttException
     */
    public void closeConnect(long timeOut) {
        try {
            if (isConnected()) {
                client.disconnect(timeOut);
            }
            log.info("已断开与服务器连接");
        } catch (MqttException e) {
            log.error("与服务器断开链接失败 {}", e.getMessage());
        }
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
