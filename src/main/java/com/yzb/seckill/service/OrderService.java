package com.yzb.seckill.service;

import com.yzb.seckill.entity.OrderInfo;

/**
 * @author: yzb
 * @date: 2020/8/2 21:29
 * @package: com.yzb.seckill.service
 * @description: 订单服务接口
 * @version: 1.0
 */
public interface OrderService {

    /**
     * 保存订单
     * @param orderInfo
     * @return
     */
    int saveOrder(OrderInfo orderInfo);
}
