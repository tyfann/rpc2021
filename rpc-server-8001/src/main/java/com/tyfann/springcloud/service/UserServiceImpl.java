package com.tyfann.springcloud.service;

import com.tyfann.springcloud.entities.IUserService;
import com.tyfann.springcloud.entities.User;
import org.springframework.stereotype.Service;

/**
 * @author tyfann
 * @date 2021/1/11 11:49 上午
 */
@Service
public class UserServiceImpl implements IUserService {

    @Override
    public String findUserById(Integer id) {
        return "Michael";
    }
}
