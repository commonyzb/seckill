package com.yzb.seckill.service;

import com.yzb.seckill.common.BaseResponse;
import com.yzb.seckill.dao.GoodsDAO;
import com.yzb.seckill.dao.OrderDAO;
import com.yzb.seckill.entity.Goods;
import com.yzb.seckill.entity.OrderInfo;
import com.yzb.seckill.utils.SnowFlakeShortUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author: yzb
 * @date: 2020/8/2 21:38
 * @package: com.yzb.seckill.service
 * @description: 基于数据库的秒杀服务
 * @version: 1.0
 */
@Service
public class DataBaseSecKillService {

    @Resource
    private GoodsDAO goodsDAO;
    @Resource
    private OrderDAO orderDAO;
    @Value("${seckill.dataCenterId}")
    private long dataCenterId;
    @Value("${seckill.machineId}")
    private long machineId;
    private SnowFlakeShortUrl snowFlakeShortUrl = new SnowFlakeShortUrl(dataCenterId,machineId);

    /**
     *
     * @param userId
     * @param goodsId
     * @throws RuntimeException
     */
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<?> secKill(int userId, int goodsId) throws RuntimeException{
        Goods goods = goodsDAO.getGoodsById(goodsId);
        if (goods.getStock() <= 0){
            throw new RuntimeException("商品已售完");
        }
        OrderInfo orderInfo = new OrderInfo();
        long orderId = snowFlakeShortUrl.nextId();
        orderInfo.setId(orderId);
        orderInfo.setUserId(userId);
        orderInfo.setGoodsId(goodsId);
        int insertNum = orderDAO.saveOrder(orderInfo);
        if (insertNum <= 0){
            throw new RuntimeException("订单创建失败");
        }
        int updateNum = goodsDAO.decrStock(goodsId);
        if (updateNum <= 0){
            throw new RuntimeException("商品已售完");
        }
        return new BaseResponse<>(0,"抢购成功！");
    }
}
