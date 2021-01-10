package com.tyfann.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author tyfann
 * @date 2021/1/10 7:26 下午
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OrderZKMain800 {
    public static void main(String[] args) {
        SpringApplication.run(OrderZKMain800.class,args);
    }
}
