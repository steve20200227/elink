package cn.snowsoft.iot.module.warning.rabbitmq;

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
     * 属性类型场景结果通知队列
     */
    @Bean
    public Queue attributeResultQueue() {
        return new Queue("iot.warning.attribute.result");
    }

    /**
     * 事件类型场景结果通知队列
     */
    @Bean
    public Queue eventResultQueue() {
        return new Queue("iot.warning.event.result");
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
     * @param attributeResultQueue
     * @return
     */
    @Bean
    public Binding bindingAttributeResultQueue(TopicExchange topicExchange, Queue attributeResultQueue) {
        return BindingBuilder.bind(attributeResultQueue).to(topicExchange).with("iot.warning.attribute.result"); // 替换为实际的路由键
    }

    /**
     * 绑定队列和交换机
     * @param topicExchange
     * @param eventResultQueue
     * @return
     */
    @Bean
    public Binding bindingEventResultQueue(TopicExchange topicExchange, Queue eventResultQueue) {
        return BindingBuilder.bind(eventResultQueue).to(topicExchange).with("iot.warning.event.result"); // 替换为实际的路由键
    }

}
