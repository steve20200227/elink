package cn.snowsoft.iot.module.warning.rabbitmq;

import cn.snowsoft.iot.module.warning.controller.admin.warning.config.vo.WarningDetailVO;
import cn.snowsoft.iot.module.warning.event.*;
import cn.snowsoft.iot.module.warning.service.warning.config.IWarningService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 启动后监听rabbit的告警通知队列，当收到新的告警信息后，解析消息并且做业务处理
 * @author chen_gang
 */
@Slf4j
@Component
public class WarningQueueHandler {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private IWarningService warningService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private WarningEventHandler eventHandler;

	/**
	 * 属性场景的结果处理
	 * 监听告警通知队列，处理告警消息
	 * @param message
	 */
	@RabbitListener(queues = "iot.warning.attribute.result")
	public void warningAttributeHandler(JSONObject message) {
		try {
			log.info("warning <===> 接收属性告警消息：{}", message);
			// 判断是否有收敛
			if (message.getJSONObject("last_value") != null) {
				message = message.getJSONObject("last_value");
			}
			distributionWarning(message);
		} catch (Exception e) {
            log.error("warning <===> 接收属性告警消息异常：{}", e);
        }
    }

	/**
	 * 事件场景的结果处理
	 * 监听告警通知队列，处理告警消息
	 * @param message
	 */
	@RabbitListener(queues = "iot.warning.event.result")
	public void warningEventHandler(JSONObject message) {
		try {
			log.info("warning <===> 接收事件告警消息：{}", message);
			// 判断是否有收敛
			if (message.getJSONObject("last_value") != null) {
				message = message.getJSONObject("last_value");
			}
			//todo 将消息体中的payload的字段进行替换，替换规则：如果当前设备的当前事件在平台处配置了输出参数，那么替换为平台配置的输出参数
			distributionWarning(message);
		} catch (Exception e) {
            log.error("warning <===> 接收事件告警消息异常：{}", e);
        }
    }

	/**
	 * 接收到告警消息后，使用事件驱动机制分别进行以下处理： 1.保存告警记录 2.执行告警动作（告警动作并行执行）
	 * 查询此告警配置的动作列表，分别发布对应事件
	 * @param message
	 */
	private void distributionWarning(JSONObject message) {
		//生成uuid,供告警记录查询动作执行记录时使用
		final String uuId = UUID.randomUUID().toString();
		//1.根据告警信息查询平台相关的告警配置和动作列表
		//从告警消息内容获取平台告警配置id
		Long warningId = message.getLong("warningId");
		//获取对应的告警配置信息和动作列表
		WarningDetailVO warningDetailVO = warningService.selectWarningById(warningId);
		//按照告警配置的优先级进行不同处理：
		// 普通：第一条告警数据接收后， 进行告警记录插入，告警动作执行（发消息、北向输出、设备反控三种都执行）。
		// 后续同场景同设备告警数据接收时，只更新告警记录，不执行告警动作。直到告警记录更新为已处理。
		// 重要：第一条告警数据接收后， 进行告警记录插入，告警动作执行（发消息、北向输出、设备反控三种都执行）。
		// 后续同场景同设备告警数据接收时，更新告警记录，执行告警动作（不执行发消息动作）。直到告警记录更新为已处理。
		// 紧急：第一条告警数据接收后， 进行告警记录插入，告警动作执行（发消息、北向输出、设备反控三种都执行）。
		// 后续同场景同设备告警数据接收时，更新告警记录，执行告警动作。直到告警记录更新为已处理。
		//由于告警动作是否执行需要根据告警记录是否存在来判断，故此处同步执行告警记录留痕处理
		boolean isNewWarning = eventHandler.handleMysqlAction(message, uuId, warningDetailVO.getId(), warningDetailVO.getWarningName());
		//遍历动作列表，逐一进行事件发布
		JSONObject finalMessage = message;
		warningDetailVO.getActionDOList().forEach(action -> {
			WarningEvent event = null;
			//根据动作的小类型区分不同事件，0-6 为消息类型，mqtt、http是北向输出，feature是设备反控
			switch (action.getOutputWay()) {
				// 当动作是 站内信、电话、短信、邮件、钉钉、企业微信、飞书
				case "0" :
				case "1" :
				case "2" :
				case "3" :
				case "4" :
				case "5" :
				case "6" :
					//Priority 1:普通 2:重要 3:紧急
					//新告警或告警级别为紧急时，发送告警消息
					if (isNewWarning || warningDetailVO.getPriority() == 3) {
						event = new MessageWarningEvent(finalMessage, action.getRelatedParameter(), uuId, action.getId(),warningDetailVO,action);
					}
					break;

				case "MQTT" :
					//新告警或告警级别是非普通，进行北向输出
					if (isNewWarning || warningDetailVO.getPriority() != 1) {
						event = new MqttWarningEvent(finalMessage, action.getRelatedParameter(), uuId, action.getId(), warningDetailVO, action);
					}
					break;

				case "HTTP" :
					//新告警或告警级别是非普通，进行北向输出
					if (isNewWarning || warningDetailVO.getPriority() != 1) {
						event = new HttpWarningEvent(finalMessage, action.getRelatedParameter(), uuId, action.getId(), warningDetailVO, action);
					}
					break;

				case "feature" :
					//新告警或告警级别是非普通，进行设备反控
					if (isNewWarning || warningDetailVO.getPriority() != 1) {
						event = new FeatureWarningEvent(finalMessage, action.getRelatedParameter(), uuId, action.getId(), warningDetailVO, action);
					}
					break;

				default:
					event = null;
			}
			//发布事件
			if (ObjectUtils.isNotEmpty(event)) {
				eventPublisher.publishEvent(event);
				log.info("发布事件完成 {}", event);
			}
		});
	}

	/**
	 * 发送消息到rabbitmq
	 */
	public void sendMessage(String routeKey, String data) {
		rabbitTemplate.convertAndSend("", routeKey, data);
	}
}
