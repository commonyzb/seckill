package com.yzb.seckill.service;

import com.yzb.seckill.entity.Goods;

import java.util.List;

/**
 * @author: yzb
 * @date: 2020/8/2 21:28
 * @package: com.yzb.seckill.service
 * @description: 商品服务接口
 * @version: 1.0
 */
public interface GoodsService {

    /**
     * 商品库存减一
     * @param id
     * @return
     */
    int descStock(int id);

    /**
     * 获取所有商品信息
     * @return
     */
    List<Goods> listGoods();
}
