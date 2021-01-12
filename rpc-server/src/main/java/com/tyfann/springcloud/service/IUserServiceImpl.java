package com.tyfann.springcloud.service;

import com.tyfann.springcloud.entities.IUserService;
import com.tyfann.springcloud.entities.User;

/**
 * @author tyfann
 * @date 2021/1/12 8:41 下午
 */
public class IUserServiceImpl implements IUserService {
    @Override
    public String findUserById(Integer id) {
        return "Michael";
    }

    @Override
    public User getUserById(Integer id) {
        return new User(id,"Michael");
    }
}
