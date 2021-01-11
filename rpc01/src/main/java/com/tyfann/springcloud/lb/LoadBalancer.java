package com.tyfann.springcloud.lb;

import java.util.List;

/**
 * @author tyfann
 * @date 2021/1/11 11:36 下午
 */
public interface LoadBalancer {
    String instances(List<String> serviceInstanceList);
}
