package com.tyfann.springcloud.controller;

import com.tyfann.springcloud.entities.IUserService;
import com.tyfann.springcloud.entities.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tyfann
 * @date 2021/1/11 6:22 下午
 */
@RestController
public class ConsumerController {

    @GetMapping(value = "/consumer/payment/zk")
    public User getUserInfoById(){
        IUserService service = ConsumerStub.getStub();
        User user = service.findUserById(123);
        System.out.println(user);
        return user;
    }
}
