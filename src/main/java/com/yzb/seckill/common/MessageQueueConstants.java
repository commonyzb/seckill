package com.yzb.seckill.common;

/**
 * @author: yzb
 * @date: 2020/8/5 20:56
 * @package: com.yzb.seckill.common
 * @description: 常量
 * @version: 1.0
 */
public interface MessageQueueConstants {
    public static final String ORDER_EXCHANGE_NAME = "local.seckill.order.exchange";
    public static final String ORDER_QUEUE_NAME = "local.seckill.order.queue";
    public static final String ORDER_ROUTING_KEY = "local.seckill.order.routingKey";
}
