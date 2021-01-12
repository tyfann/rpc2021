package com.tyfann.springcloud.controller;

import com.tyfann.springcloud.entities.IUserService;
import com.tyfann.springcloud.service.IUserServiceImpl01;
import com.tyfann.springcloud.service.IUserServiceImpl02;
import lombok.SneakyThrows;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author tyfann
 * @date 2021/1/12 12:32 下午
 */
public class CuratorCreate {
    private static boolean running = true;
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

    }

    public void createNode(int port) throws Exception {
        final Base64.Encoder encoder = Base64.getEncoder();
        final String text = zkServerPath+":"+port;
        final byte[] ipByte = text.getBytes(StandardCharsets.UTF_8);
        final String encodeIp = encoder.encodeToString(ipByte);
        client.checkExists()
                .forPath("/rpc-payment-service");
        client.create()
                .withMode(CreateMode.EPHEMERAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/rpc-payment-service/"+encodeIp,encodeIp.getBytes(StandardCharsets.UTF_8));
    }

    public void listen(ServerSocket serverSocket,int serviceSequence) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
           @SneakyThrows
           @Override
           public void run() {
               client.close();
           }
        });
        while (running) {
            Socket socketClient = serverSocket.accept();
            process(socketClient,serviceSequence);
            socketClient.close();
        }
    }

    public static void process(Socket socket,int serviceSequence) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        IUserService service;
        //为了适应客户端通用化而做的改动
        String methodName = ois.readUTF();
        Class[] parameterTypes = (Class[]) ois.readObject();
        Object[] parameters = (Object[]) ois.readObject();

        switch (serviceSequence) {
            case 1: {
                service = new IUserServiceImpl01();//服务类型暂时还是写死的，不够灵活
                break;
            }
            case 2: {
                service = new IUserServiceImpl02();//服务类型暂时还是写死的，不够灵活
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + serviceSequence);
        }

        Method method = service.getClass().getMethod(methodName, parameterTypes);
        Object Obj = method.invoke(service, parameters);
        oos.writeObject(Obj);
        oos.flush();
    }

}
