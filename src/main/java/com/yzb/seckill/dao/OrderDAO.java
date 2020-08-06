package com.yzb.seckill.dao;

import com.yzb.seckill.entity.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * @author: yzb
 * @date: 2020/8/2 21:18
 * @package: com.yzb.seckill.dao
 * @description: 订单DAO
 * @version: 1.0
 */
@Mapper
public interface OrderDAO {

    /**
     * 保存订单信息
     * @param orderInfo
     * @return
     */
    @Options(keyColumn = "id",keyProperty = "id")
    @Insert("INSERT INTO order_info(id,user_id,goods_id) VALUES(#{id},#{userId},#{goodsId})")
    int saveOrder(OrderInfo orderInfo);

}
