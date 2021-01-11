package com.tyfann.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author tyfann
 * @date 2021/1/11 11:00 下午
 */
public interface LoadBalancer {
    String instances(List<String> serviceInstanceList);
}
