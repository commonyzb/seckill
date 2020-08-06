package com.yzb.seckill.config;

import com.yzb.seckill.common.MessageQueueConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: yzb
 * @date: 2020/8/5 15:06
 * @package: com.yzb.seckill.config
 * @description: 消息队列配置
 * @version: 1.0
 */

@Configuration
public class RabbitMQConfig {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);

//    private String exchangeName = MessageQueueConstants.ORDER_EXCHANGE_NAME;
//    private String queueName = MessageQueueConstants.ORDER_QUEUE_NAME;
//    private String routingKey = MessageQueueConstants.ORDER_ROUTING_KEY;

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory cachingConnectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        //设置消息发送格式为json
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        //消息发送到exchange回调 需设置：spring.rabbitmq.publisher-confirms=true
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                logger.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
            }
        });
        //消息从exchange发送到queue失败回调  需设置：spring.rabbitmq.publisher-returns=true
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                logger.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
            }
        });
        return rabbitTemplate;
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(CachingConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //json序列化时，若想手动ACK，则必须配置
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public TopicExchange orderTopicExchange(){
        return new TopicExchange(MessageQueueConstants.ORDER_EXCHANGE_NAME,true,false);
    }

    @Bean
    public Queue orderQueue(){
        return new Queue(MessageQueueConstants.ORDER_QUEUE_NAME,true);
    }

    @Bean
    public Binding simpleBinding(){
        return BindingBuilder.bind(orderQueue()).to(orderTopicExchange()).with(MessageQueueConstants.ORDER_ROUTING_KEY);
    }
}
