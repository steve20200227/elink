package cn.snowsoft.iot.module.warning.mqtt;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/**
 * mqtt客户端回调类
 *
 * @author chen_gang
 * @date 2022/11/15
 */
@Slf4j
@AllArgsConstructor
public class MqttClientCallback implements MqttCallbackExtended {

	private MqttClientUtil client;

	@Override
	public void connectComplete(boolean reconnect, String serverurl) {
		log.info("已连接到服务器 {}", serverurl);
	}

	@Override
	public void connectionLost(Throwable cause) {
		log.warn("客户端失去链接 {}", cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws UnsupportedEncodingException {
		log.info("接收到{} 消息 {}", topic, new String(message.getPayload(), "UTF-8"));
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		log.info("发布消息已送达");
	}
}
