package com.tyfann.springcloud.controller;

import com.tyfann.springcloud.entities.IUserService;
import com.tyfann.springcloud.entities.User;
import com.tyfann.springcloud.lb.LoadBalancer;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

/**
 * @author tyfann
 * @date 2021/1/11 6:22 下午
 */
@RestController
public class ConsumerController {

    CuratorFramework client;
    final String IP = "192.168.1.106:2181";
    final String Path = "/services";
    int queryNum = 123;

    @Resource
    private LoadBalancer loadBalancer;

    @GetMapping(value = "/consumer/payment/zk")
    public String getUserInfoById() throws Exception {
        //重试策略，每隔1000ms重试一次，总共重试10次
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
        client = CuratorFrameworkFactory.builder()
                .connectString(IP)   //zookeeper服务器地址
                .retryPolicy(retryPolicy)   //重试策略
                .connectionTimeoutMs(30 * 1000)  //连接超时时间
                .sessionTimeoutMs(3 * 1000)   //会话超时时间
                .build();

        client.start();

        final String serviceName = "rpc-payment-service";
        String serviceAddress = discover(serviceName);
        String ip = serviceAddress.split("[:]")[0];
        int port = Integer.parseInt(serviceAddress.split("[:]")[1]);

        IUserService service = ConsumerStub.getStub(ip,port);
        String userName = service.findUserById(queryNum);
        System.out.println("服务调用成功！结果是：  "+userName);
        return userName;
    }

    public String discover(String serviceName) throws Exception {
        String servicePath = Path+"/"+serviceName;
        List<String> childrenNames = client.getChildren().forPath(servicePath);

        Base64.Decoder decoder = Base64.getDecoder();
        Iterator<String> it = childrenNames.iterator();
        List<String> nodeNames = new ArrayList<>();
        while (it.hasNext()){
            String s = it.next();
            System.out.println(new String(decoder.decode(s),StandardCharsets.UTF_8));
            nodeNames.add(new String(decoder.decode(s),StandardCharsets.UTF_8));
        }

        if (nodeNames.size() <= 0) {
            return null;
        }


        return loadBalancer.instances(nodeNames);
    }
}
