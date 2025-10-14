package cn.snowsoft.iot.module.cps.emqx.client;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * paho客户端回调类
 *
 * @author chen_gang
 * @date 2022/11/15
 */
@Slf4j
public class PahoCallback implements MqttCallbackExtended {
	private PahoClient client;

	public PahoCallback(PahoClient client) {
		this.client = client;
	}

	@Override
	public void connectComplete(boolean reconnect, String serverurl) {
		log.info("已连接到服务器 {}", serverurl);
	}

	@Override
	public void connectionLost(Throwable cause) {
		log.warn("客户端失去链接 {}", cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		String msg = new String(message.getPayload());
		log.info("MQTT客户端 接收消息  {}", msg);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		log.info("发布消息已送达");
	}
}
