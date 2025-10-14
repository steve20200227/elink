package cn.snowsoft.iot.module.cps.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置，主要是配置队列，如果提前存在该队列，可以省略本配置类
 *
 * @author chen_gang
 */
@Slf4j
@Configuration
public class RabbitMqConfiguration {
	/**
	 * 设备事件上报队列，在此处创建该队列，主要用于设备事件上报数据的存储
	 */
	@Bean
	public Queue deviceEventQueue() {
		return new Queue("iot.event");
	}

	/**
	 * 设备上下线队列，主要用于获取设备上下线消息并进行设备状态更新
	 */
	@Bean
	public Queue deviceStatusQueue() {
		return new Queue("iot.device.status");
	}

	/**
	 * RabbitMQ默认的topic交换机
	 * @return
	 */
	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange("amq.topic");
	}

	/**
	 * 绑定队列和交换机
	 * @param topicExchange
	 * @param deviceEventQueue
	 * @return
	 */
	@Bean
	public Binding bindingDeviceEventQueue(TopicExchange topicExchange, Queue deviceEventQueue) {
		return BindingBuilder.bind(deviceEventQueue).to(topicExchange).with("iot.event"); // 替换为实际的路由键
	}

	/**
	 * 绑定队列和交换机
	 * @param topicExchange
	 * @param deviceStatusQueue
	 * @return
	 */
	@Bean
	public Binding bindingDeviceStatusQueue(TopicExchange topicExchange, Queue deviceStatusQueue) {
		return BindingBuilder.bind(deviceStatusQueue).to(topicExchange).with("iot.device.status"); // 替换为实际的路由键
	}
}
