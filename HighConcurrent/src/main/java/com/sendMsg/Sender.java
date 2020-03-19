package com.sendMsg;
import lombok.Data;

@Data
public class Sender {

    void send(String msg,AbstractSendMsg sendMsg){
        new Thread(()->sendMsg.sendStatus(msg)).start();
    }
}