package com.sendMsg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;


/**
 * 发送消息service层
 */
@Slf4j
@Service
public class SendMsgService {

    /**
     * 发送到服务器端的方法
     * @param msg
     * @throws InterruptedException
     */
    public void sendServer(String msg) throws InterruptedException {
        Sender sender = new Sender();
        CountDownLatch cont = new CountDownLatch(1);
        sender.send(msg, new AbstractSendMsg() {
            @Override
            public void success(String msg) {
                log.info("msg：{} 发送成功！",msg);
                cont.countDown();
            }

            @Override
            public void failure(Throwable e, String msg) {
                log.info("msg：{} 发送失败！",msg);
                log.error("失败原因：",e);
                cont.countDown();
            }
        });

        log.info("处理自己的业务逻辑～～～～");
        Thread.sleep(1000);
        cont.await();
        log.info("完成发送！！！！");
    }
}