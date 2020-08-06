package com.yzb.seckill.service.impl;

import com.yzb.seckill.dao.OrderDAO;
import com.yzb.seckill.entity.OrderInfo;
import com.yzb.seckill.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author: yzb
 * @date: 2020/8/2 21:36
 * @package: com.yzb.seckill.service.impl
 * @description: 订单服务
 * @version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDAO orderDAO;

    @Override
    public int saveOrder(OrderInfo orderInfo) {
        return orderDAO.saveOrder(orderInfo);
    }

}
