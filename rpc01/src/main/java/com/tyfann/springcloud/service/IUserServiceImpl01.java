package com.tyfann.springcloud.service;

import com.tyfann.springcloud.entities.IUserService;

/**
 * @author tyfann
 * @date 2021/1/12 12:12 上午
 */
public class IUserServiceImpl01 implements IUserService {
    @Override
    public String findUserById(Integer id) {
        return id.toString() + "Michael";
    }
}
