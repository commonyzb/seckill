package com.yzb.seckill.service;

import com.yzb.seckill.dao.GoodsDAO;
import com.yzb.seckill.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: yzb
 * @date: 2020/8/4 12:16
 * @package: com.yzb.seckill.service
 * @description: 缓存预热
 * @version: 1.0
 */
@Service
public class CachePreheating {

    @Resource
    private GoodsDAO goodsDAO;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static final String SECKILL_GOODS_BASE_KEY = "seckill:goods:id:";

    public void doCachePreheating(int id){
        Goods goods = goodsDAO.getGoodsById(id);
        redisTemplate.opsForValue().set(SECKILL_GOODS_BASE_KEY+goods.getId(),goods.getStock());
    }
}
