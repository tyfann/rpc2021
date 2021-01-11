package com.tyfann.springcloud;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

/**
 * @author tyfann
 * @date 2021/1/11 9:26 下午
 */
public class CuratorGetter {

    CuratorFramework client;
    final String IP = "192.168.1.106:2181";
    final String Path = "/services/rpc-payment-service";

    @Before
    public void before() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)   //zookeeper服务器地址
                .retryPolicy(retryPolicy)   //重试策略
                .connectionTimeoutMs(30 * 1000)  //连接超时时间
                .sessionTimeoutMs(3 * 1000)   //会话超时时间
                .build();

        client.start();
    }

    @After
    public void after() {
        client.close();
    }

    @Test
    public void get1() throws Exception {
        List<String> childrenNames = client.getChildren().forPath(Path);

        Base64.Decoder decoder = Base64.getDecoder();
        Iterator<String> it = childrenNames.iterator();
        List<String> nodeNames = new ArrayList<>();
        while (it.hasNext()){
            String s = it.next();
            System.out.println(new String(decoder.decode(s),StandardCharsets.UTF_8));
            nodeNames.add(new String(decoder.decode(s),StandardCharsets.UTF_8));
        }
        String serviceAddress = nodeNames.get(0);
        String ip = serviceAddress.split("[:]")[0];
        String port = serviceAddress.split("[:]")[1];
        System.out.println("服务ip是：  "+ip);
        System.out.println("服务端口是：  "+port);
    }
}