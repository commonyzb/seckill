package com.yzb.seckill.entity;

import java.io.Serializable;

/**
 * @author: yzb
 * @date: 2020/8/2 21:16
 * @package: com.yzb.seckill.entity
 * @description: 订单
 * @version: 1.0
 */
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = -2672969123822576215L;

    private Long id;
    private Integer userId;
    private Integer goodsId;

    public OrderInfo() {
    }

    public OrderInfo(Integer userId, Integer goodsId) {
        this.userId = userId;
        this.goodsId = goodsId;
    }

    public OrderInfo(Long id, Integer userId, Integer goodsId) {
        this.id = id;
        this.userId = userId;
        this.goodsId = goodsId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", goodsId=" + goodsId +
                '}';
    }
}
