package com.yzb.seckill.controller;

import com.yzb.seckill.common.BaseResponse;
import com.yzb.seckill.entity.Goods;
import com.yzb.seckill.entity.OrderInfo;
import com.yzb.seckill.service.DataBaseSecKillService;
import com.yzb.seckill.service.GoodsService;
import com.yzb.seckill.service.RabbitSecKill;
import com.yzb.seckill.service.RedisSecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author: yzb
 * @date: 2020/8/2 22:03
 * @package: com.yzb.seckill.controller
 * @description: 秒杀控制器
 * @version: 1.0
 */
@RestController
public class SecKillController {

    @Autowired
    private DataBaseSecKillService secKillService;
    @Autowired
    private RedisSecKillService redisSecKillService;
    @Autowired
    private RabbitSecKill rabbitSecKill;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private static final String SECKILL_GOODS_BASE_KEY = "seckill:goods:id:";

    /**
     * 启动程序，缓存预热
     */
    @PostConstruct
    public void cachePreheating(){
        List<Goods> listGoods = goodsService.listGoods();
        for (Goods goods:listGoods) {
            redisTemplate.opsForValue().set(SECKILL_GOODS_BASE_KEY+goods.getId(),goods.getStock());
        }
    }

    @RequestMapping("/secKill/{userId}/{goodsId}")
    public BaseResponse<?> secKill2(@PathVariable int userId,@PathVariable int goodsId){
        //使用redis做缓存预减库存，rabbitmq异步下单，减轻数据库压力
        try {
            OrderInfo orderInfo = new OrderInfo(userId,goodsId);
            return rabbitSecKill.secKill(orderInfo);
        } catch ( Exception e) {
            //数据库减库存或者创建订单失败时，将redis预减的1个库存还原
            redisTemplate.opsForValue().increment(SECKILL_GOODS_BASE_KEY+goodsId);
            return new BaseResponse<>(1,e.getMessage());
        }
    }

    @RequestMapping("/secKill2/{userId}/{goodsId}")
    public BaseResponse<?> secKill1(@PathVariable int userId,@PathVariable int goodsId){
        //使用redis做缓存预减库存，减轻数据库压力
        try {
            return redisSecKillService.secKill(userId,goodsId);
        } catch ( Exception e) {
            //数据库减库存或者创建订单失败时，将redis预减的1个库存还原
            redisTemplate.opsForValue().increment(SECKILL_GOODS_BASE_KEY+goodsId);
            return new BaseResponse<>(1,e.getMessage());
        }
    }

    @RequestMapping("/secKill3/{userId}/{goodsId}")
    public BaseResponse<?> secKill(@PathVariable int userId,@PathVariable int goodsId){
        try {
            BaseResponse<?> baseResponse = secKillService.secKill(userId,goodsId);
            return baseResponse;
        } catch ( Exception e) {
            return new BaseResponse<>(1,e.getMessage());
        }
    }




}
