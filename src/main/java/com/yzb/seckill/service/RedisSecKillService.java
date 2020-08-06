package com.yzb.seckill.service;

import com.yzb.seckill.common.BaseResponse;
import com.yzb.seckill.dao.GoodsDAO;
import com.yzb.seckill.dao.OrderDAO;
import com.yzb.seckill.entity.OrderInfo;
import com.yzb.seckill.utils.SnowFlakeShortUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author: yzb
 * @date: 2020/8/3 21:16
 * @package: com.yzb.seckill.service
 * @description: Redis做缓存优化秒杀
 * @version: 1.0
 */
@Service
public class RedisSecKillService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private CachePreheating cachePreheating;
    @Resource
    private OrderDAO orderDAO;
    @Resource
    private GoodsDAO goodsDAO;
    private static final String SECKILL_GOODS_BASE_KEY = "seckill:goods:id:";
    @Value("${seckill.dataCenterId}")
    private long dataCenterId;
    @Value("${seckill.machineId}")
    private long machineId;
    private SnowFlakeShortUrl snowFlakeShortUrl = new SnowFlakeShortUrl(dataCenterId,machineId);

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<?> secKill(int userId,int goodsId){
        Long stock = redisTemplate.opsForValue().decrement(SECKILL_GOODS_BASE_KEY+goodsId);
        if (stock < 0){
            //防止少卖，
            redisTemplate.opsForValue().increment(SECKILL_GOODS_BASE_KEY+goodsId);
            return new BaseResponse<>(1,"库存不足");
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(snowFlakeShortUrl.nextId());
        orderInfo.setGoodsId(goodsId);
        orderInfo.setUserId(userId);
        int insertNum = orderDAO.saveOrder(orderInfo);
        if (insertNum <= 0){
//            redisTemplate.opsForValue().increment(SECKILL_GOODS_BASE_KEY+goodsId);
            throw new RuntimeException("订单生成失败！");
        }
        int updateNum = goodsDAO.decrStock(goodsId);
        if (updateNum <= 0){
//            redisTemplate.opsForValue().increment(SECKILL_GOODS_BASE_KEY+goodsId);
            throw new RuntimeException("商品已售完");
        }
        return new BaseResponse<>(0,"抢购成功");
    }

}
