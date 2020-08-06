package com.yzb.seckill.entity;

import java.io.Serializable;

/**
 * @author: yzb
 * @date: 2020/8/2 21:14
 * @package: com.yzb.seckill.entity
 * @description: 商品信息
 * @version: 1.0
 */
public class Goods implements Serializable {
    private static final long serialVersionUID = -8190906463382832235L;

    private Integer id;
    private String name;
    private Integer stock;

    public Goods() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                '}';
    }
}
