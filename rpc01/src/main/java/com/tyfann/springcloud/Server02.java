package com.tyfann.springcloud;

import com.tyfann.springcloud.entities.IUserService;
import com.tyfann.springcloud.entities.User;
import com.tyfann.springcloud.service.IUserServiceImpl02;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author tyfann
 * @date 2021/1/11 11:34 下午
 */
public class Server02 {
    private static boolean running = true;
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(8002);
        while(running){
            Socket client = server.accept();
            process(client);
            client.close();
        }
        server.close();
    }
    public static void process(Socket socket) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        //为了适应客户端通用化而做的改动
        String methodName = ois.readUTF();
        Class[] parameterTypes = (Class[]) ois.readObject();
        Object[] parameters = (Object[]) ois.readObject();

        IUserService service = new IUserServiceImpl02();//服务类型暂时还是写死的，不够灵活
        Method method = service.getClass().getMethod(methodName, parameterTypes);
        String userName = (String) method.invoke(service, parameters);
        oos.writeObject(userName);
        oos.flush();
    }
}
