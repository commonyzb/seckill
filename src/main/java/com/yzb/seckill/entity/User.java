package com.yzb.seckill.entity;

import java.io.Serializable;

/**
 * @author: yzb
 * @date: 2020/8/2 21:11
 * @package: com.yzb.seckill.entity
 * @description: 用户信息
 * @version: 1.0
 */
public class User implements Serializable {

    private static final long serialVersionUID = 6789340823037912679L;
    private Integer id;
    private String name;
    private String password;

    public User() {
    }

    public User(Integer id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
