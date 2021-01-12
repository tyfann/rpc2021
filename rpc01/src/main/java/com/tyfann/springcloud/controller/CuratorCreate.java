package com.tyfann.springcloud.controller;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author tyfann
 * @date 2021/1/12 12:32 下午
 */
public class CuratorCreate {
    CuratorFramework client;
    final String IP = "192.168.1.106:2181";
    public static final String zkServerPath = "127.0.0.1";


    public void create(int port) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)   //zookeeper服务器地址
                .retryPolicy(retryPolicy)   //重试策略
                .namespace("services")
                .connectionTimeoutMs(30 * 1000)  //连接超时时间
                .sessionTimeoutMs(3 * 1000)   //会话超时时间
                .build();

        client.start();

        createNode(port);

        client.close();

    }

    public void createNode(int port) throws Exception {
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = zkServerPath+":"+port;
        final byte[] ipByte = text.getBytes(StandardCharsets.UTF_8);
        final String encodeIp = encoder.encodeToString(ipByte);
        client.checkExists()
                .forPath("/rpc-payment-service");
        client.create()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/rpc-payment-service/"+encodeIp,encodeIp.getBytes(StandardCharsets.UTF_8));
    }

    public void delete(int port) throws Exception {
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = zkServerPath+":"+port;
        final byte[] ipByte = text.getBytes(StandardCharsets.UTF_8);
        final String encodeIp = encoder.encodeToString(ipByte);
        client.delete()
                .forPath("/rpc-payment-service/"+encodeIp);
    }
}
