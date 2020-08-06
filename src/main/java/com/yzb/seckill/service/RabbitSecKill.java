package com.yzb.seckill.service;

import com.yzb.seckill.common.BaseResponse;
import com.yzb.seckill.common.MessageQueueConstants;
import com.yzb.seckill.entity.OrderInfo;
import com.yzb.seckill.utils.SnowFlakeShortUrl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author: yzb
 * @date: 2020/8/5 20:40
 * @package: com.yzb.seckill.service
 * @description: 使用rabbitmq消息队列异步秒杀下单
 * @version: 1.0
 */
@Service
public class RabbitSecKill {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static final String SECKILL_GOODS_BASE_KEY = "seckill:goods:id:";
    @Value("${seckill.dataCenterId}")
    private long dataCenterId;
    @Value("${seckill.machineId}")
    private long machineId;
    private SnowFlakeShortUrl snowFlakeShortUrl = new SnowFlakeShortUrl(dataCenterId,machineId);

    public BaseResponse<?> secKill(OrderInfo orderInfo){
        int goodsId = orderInfo.getGoodsId();
        Long stock = redisTemplate.opsForValue().decrement(SECKILL_GOODS_BASE_KEY+goodsId);
        if (stock < 0){
            //防止少卖，
            redisTemplate.opsForValue().increment(SECKILL_GOODS_BASE_KEY+goodsId);
            return new BaseResponse<>(1,"库存不足");
        }
        orderInfo.setId(snowFlakeShortUrl.nextId());
        rabbitTemplate.convertAndSend(MessageQueueConstants.ORDER_EXCHANGE_NAME,MessageQueueConstants.ORDER_ROUTING_KEY,orderInfo);
        return new BaseResponse<>(0,"排队中...");
    }
}
