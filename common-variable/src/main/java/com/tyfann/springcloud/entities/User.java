package com.tyfann.springcloud.entities;

import java.io.Serializable;

/**
 * @author tyfann
 * @date 2021/1/10 6:46 下午
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
