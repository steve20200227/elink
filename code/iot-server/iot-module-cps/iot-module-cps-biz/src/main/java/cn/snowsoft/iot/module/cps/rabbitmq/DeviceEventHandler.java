package cn.snowsoft.iot.module.cps.rabbitmq;

import cn.snowsoft.iot.module.cps.dal.dataobject.eventRecord.EventRecordDO;
import cn.snowsoft.iot.module.cps.dal.mysql.eventRecord.EventRecordMapper;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

/**
 * 设备事件上报处理器，用于设备事件数据的持久化留存
 * @author raft
 */
@Slf4j
@Component
public class DeviceEventHandler {
	@Autowired
	private EventRecordMapper eventRecordMapper;

	@Autowired
	private RedisCacheManager redisCacheManager;
	/**
	 * 监听设备事件上报队列，将事件消息持久化到mysql中
	 * @param message
	 * {
	 *     "eventCode": "u_emqx",
	 *     "deviceCode": "c_emqx",
	 * }
	 */
	@RabbitListener(queues = "iot.event")
	public void deviceEventHandler(JSONObject message) {
		try {
			log.info("消息队列，拉取设备事件数据：{}", message);
			saveEventRecord(message);
			log.info("事件记录持久化到mysql成功：{}", message);
		} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * 监听设备上下线消息，并将设备状态存储到redis中
	 * @param message
	 * {
	 *   "msg": {
	 *     "timestamp": 1753868522560,
	 *     "peername": "127.0.0.1:52918",
	 *     "event": "client.connected",
	 *     "username": "u_emqx",
	 *     "clientid": "c_emqx",
	 *     "connected_at": 1753868522560,
	 *     "status": 1
	 *   }
	 * }
	 */
	@RabbitListener(queues = "iot.device.status")
	public void deviceStatusHandler(JSONObject message) {
		try {
			log.info("消息队列，拉取设备状态数据：{}", message);
			updateDeviceStatus(message.getJSONObject("msg"));
			log.info("设备状态数据更新redis成功：{}", message);
		} catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


	private void saveEventRecord(JSONObject message) {
		EventRecordDO eventRecordDO = new EventRecordDO();
		String eventCode = message.getString("eventCode");
		String deviceCode = message.getString("deviceCode");
		eventRecordDO.setEventCode(eventCode);
		eventRecordDO.setDeviceCode(deviceCode);
		eventRecordDO.setPayload(message.toJSONString());
		//todo需要切换为时序库存储
		eventRecordMapper.insert(eventRecordDO);
	}

	/**
	 * 更新redis中设备状态缓存
	 * @param message
	 */
	private void updateDeviceStatus(JSONObject message) {
		redisCacheManager.getCache("deviceStatus").put(message.getString("clientid"),
				message.getString("status") + "-" + message.getString("peername") + "-" + message.getString("timestamp"));
	}

}
