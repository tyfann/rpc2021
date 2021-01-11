package com.tyfann.springcloud.service;

import com.tyfann.springcloud.entities.IUserService;
import com.tyfann.springcloud.entities.User;
import org.springframework.stereotype.Service;

/**
 * @author tyfann
 * @date 2021/1/11 11:52 上午
 */
@Service
public class UserServiceImpl implements IUserService {
    @Override
    public User findUserById(Integer id) {
        return new User(id,"Michael");
    }
}
