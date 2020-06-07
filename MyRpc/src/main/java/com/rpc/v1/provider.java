package com.rpc.v1;
import	java.io.ObjectOutputStream;
import java.io.*;

import com.rpc.common.IOrderService;
import com.rpc.common.Order;

import	java.net.Socket;

import java.net.ServerSocket;

/**
 * 服务提供方
 */
public class provider {

    public static boolean isRunning = true;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);
        while(isRunning){
            Socket socket = serverSocket.accept();
            process(socket);
            socket.close();
        }
        serverSocket.close();
    }

    public static void process(Socket s) throws IOException {

        InputStream is = s.getInputStream();
        OutputStream os = s.getOutputStream();

        DataInputStream dis = new DataInputStream(is);
        ObjectOutputStream oos = new ObjectOutputStream(os);

        int orderId = dis.readInt();

        IOrderService service = new OrderServiceImpl();
        Order order = service.findOrderById(orderId);

        oos.writeObject(order);
        oos.flush();
    }
}