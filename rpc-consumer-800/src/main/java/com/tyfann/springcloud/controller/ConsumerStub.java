package com.tyfann.springcloud.controller;

import com.tyfann.springcloud.entities.IUserService;
import com.tyfann.springcloud.entities.User;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author tyfann
 * @date 2021/1/10 10:55 下午
 */
public class ConsumerStub {

    public static final String INVOKE_URL = "http://rpc-server-payment";

    static IUserService getStub(String ip, int port) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket(ip,port);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                String methodName = method.getName();
                Class[] parametersTypes = method.getParameterTypes();
                oos.writeUTF(methodName);
                oos.writeObject(parametersTypes);
                oos.writeObject(args);
                oos.flush();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object obj = ois.readObject();

                oos.close();
                socket.close();
                return obj;
            }
        };
        Object o = Proxy.newProxyInstance(IUserService.class.getClassLoader(), new Class[]{IUserService.class},handler);
        return (IUserService) o;
    }
}
