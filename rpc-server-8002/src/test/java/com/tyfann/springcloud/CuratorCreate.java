package com.tyfann.springcloud;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author tyfann
 * @date 2021/1/11 9:03 下午
 */
public class CuratorCreate {

    CuratorFramework client;
    final String IP = "192.168.1.106:2181";

    @Before
    public void before() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)   //zookeeper服务器地址
                .retryPolicy(retryPolicy)   //重试策略
                .namespace("services")
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
    public void create1() throws Exception {
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = "127.0.0.1:8002";
        final byte[] ipByte = text.getBytes(StandardCharsets.UTF_8);
        final String encodeIp = encoder.encodeToString(ipByte);
        client.create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/rpc-payment-service/"+encodeIp,encodeIp.getBytes("UTF-8"));
        System.out.println("结束");
    }
}
