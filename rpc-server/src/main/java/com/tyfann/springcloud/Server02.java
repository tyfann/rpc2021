package com.tyfann.springcloud;

import com.tyfann.springcloud.controller.CuratorCreate;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * @author tyfann
 * @date 2021/1/11 11:34 下午
 */
public class Server02 {
//    static final int serviceSequence = 2;
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(0,1, InetAddress.getByAddress(new byte[]{127,0,0,1}));
        int port = server.getLocalPort();
        CuratorCreate curatorCreate = new CuratorCreate();
        curatorCreate.create(port);

        curatorCreate.listen(server);
        server.close();

    }
}
