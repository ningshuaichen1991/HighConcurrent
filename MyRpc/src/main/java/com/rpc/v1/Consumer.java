package com.rpc.v1;
import com.rpc.common.Order;
import lombok.extern.slf4j.Slf4j;

import	java.io.ObjectInputStream;
import	java.io.DataOutputStream;
import	java.io.ByteArrayOutputStream;
import java.io.IOException;
import	java.net.Socket;

/**
 * 调用方
 */
public class Consumer {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Socket socket = new Socket("127.0.0.1",8080);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(123);

        socket.getOutputStream().write(bos.toByteArray());
        socket.getOutputStream().flush();

        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Order order = (Order)ois.readObject();

        System.out.println("远程调用结果："+order.toString());

        dos.close();
        socket.close();
    }
}