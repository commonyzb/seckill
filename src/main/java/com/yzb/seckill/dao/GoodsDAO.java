package com.yzb.seckill.dao;

import com.yzb.seckill.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author: yzb
 * @date: 2020/8/2 21:20
 * @package: com.yzb.seckill.dao
 * @description: 商品DAO
 * @version: 1.0
 */
@Mapper
public interface GoodsDAO {

    /**
     * 商品库存减一
     * @param id 商品id
     * @return 执行成功条数
     */
    @Update(" UPDATE goods SET stock=stock-1 WHERE id=#{id} AND stock>0 ")
    int decrStock(int id);

    /**
     * 通过id获取商品信息
     * @param id
     * @return
     */
    @Select(" SELECT * FROM goods WHERE id=#{id} ")
    Goods getGoodsById(int id);

    /**
     * 获取所有商品信息
     * @return
     */
    @Select("SELECT * FROM goods")
    List<Goods> listGoods();
}
