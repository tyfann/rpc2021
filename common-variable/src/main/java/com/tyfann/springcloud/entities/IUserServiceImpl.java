package com.tyfann.springcloud.entities;

/**
 * @author tyfann
 * @date 2021/1/10 7:17 下午
 */
public class IUserServiceImpl implements IUserService{
    @Override
    public User findUserById(Integer id) {
        return new User(id,"Michael");
    }
}
