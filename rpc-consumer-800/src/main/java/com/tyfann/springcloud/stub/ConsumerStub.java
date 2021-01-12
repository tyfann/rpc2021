package com.tyfann.springcloud.stub;

import com.tyfann.springcloud.entities.IUserService;
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

    public static Object getStub(String serviceAddress,Class c) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                String ip = serviceAddress.split("[:]")[0];
                int port = Integer.parseInt(serviceAddress.split("[:]")[1]);
                Socket socket = new Socket(ip,port);

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                String className = c.getName();
                String methodName = method.getName();
                Class[] parametersTypes = method.getParameterTypes();

                oos.writeUTF(className);
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
        return Proxy.newProxyInstance(c.getClassLoader(), new Class[]{c},handler);
    }
}
