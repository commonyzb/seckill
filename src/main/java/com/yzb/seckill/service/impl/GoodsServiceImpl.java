package com.yzb.seckill.service.impl;

import com.yzb.seckill.dao.GoodsDAO;
import com.yzb.seckill.entity.Goods;
import com.yzb.seckill.service.GoodsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: yzb
 * @date: 2020/8/2 21:34
 * @package: com.yzb.seckill.service.impl
 * @description: 商品服务
 * @version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsServiceImpl implements GoodsService {

    @Resource
    GoodsDAO goodsDAO;

    @Override
    public int descStock(int id) {
        return goodsDAO.decrStock(id);
    }

    @Override
    public List<Goods> listGoods() {
        return goodsDAO.listGoods();
    }
}
