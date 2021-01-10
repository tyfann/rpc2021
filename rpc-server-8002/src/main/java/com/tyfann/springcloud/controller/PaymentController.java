package com.tyfann.springcloud.controller;

import com.tyfann.springcloud.entities.IUserService;
import com.tyfann.springcloud.entities.IUserServiceImpl;
import com.tyfann.springcloud.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author tyfann
 * @date 2021/1/10 11:12 下午
 */
@RestController
@Slf4j
public class PaymentController {
    private static boolean running = true;
    @Resource
    private IUserService service;

    @GetMapping(value = "/payment/zk")
    public void getUserById() throws Exception{
        ServerSocket server = new ServerSocket(8002);
        while (running) {
            Socket client = server.accept();
            process(client);
            client.close();
        }
        server.close();
    }

    private void process(Socket socket) throws Exception{
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        String methodName = ois.readUTF();
        Class[] parameterTypes = (Class[]) ois.readObject();
        Object[] args = (Object[]) ois.readObject();

        IUserService service = new IUserServiceImpl();
        Method method = service.getClass().getMethod(methodName, parameterTypes);
        User user = (User) method.invoke(service, args);

        oos.writeObject(user);
        oos.flush();
    }

}