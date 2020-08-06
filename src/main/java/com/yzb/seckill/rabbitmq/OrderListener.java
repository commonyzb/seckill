package com.yzb.seckill.rabbitmq;

import com.rabbitmq.client.Channel;
import com.yzb.seckill.common.MessageQueueConstants;
import com.yzb.seckill.entity.OrderInfo;
import com.yzb.seckill.service.DataBaseSecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @author: yzb
 * @date: 2020/8/5 19:27
 * @package: com.yzb.seckill.rabbitmq
 * @description: 订单消息消费者
 * @version: 1.0
 */
@Component
@RabbitListener(queues = MessageQueueConstants.ORDER_QUEUE_NAME)
public class OrderListener{

    private static final Logger logger = LoggerFactory.getLogger(OrderListener.class);
    @Autowired
    private DataBaseSecKillService dataBaseSecKillService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Value("${seckill.goods.id.base.key}")
    private String secKillGoodsBaseKey;

    @RabbitHandler
    public void onMessage(OrderInfo orderInfo, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws Exception {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), orderInfo);
        try {
            dataBaseSecKillService.secKill(orderInfo.getUserId(),orderInfo.getGoodsId());
            channel.basicAck(deliveryTag,false);
        } catch (Exception e) {
            redisTemplate.opsForValue().increment(secKillGoodsBaseKey+orderInfo.getGoodsId());
            logger.error("消息监听确认机制发生异常：",e.fillInStackTrace());
        }

    }
}
