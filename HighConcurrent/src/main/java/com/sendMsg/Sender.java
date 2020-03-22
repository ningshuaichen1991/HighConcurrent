package com.sendMsg;
import com.remoteCall.ThreadPoolCommon;
import lombok.Data;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 发送者类
 */
@Data
public class Sender {

    /**
     * 异步发送消息
     * @param msg
     * @param sendMsg
     */
    void send(String msg,AbstractSendMsg sendMsg){
        ThreadPoolExecutor threadPool = ThreadPoolCommon.getThreadPoolExecutor();
        threadPool.execute(()->sendMsg.sendStatus(msg));
    }
}