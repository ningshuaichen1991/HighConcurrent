package com.sendMsg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Slf4j
@Service
public class SendMsgService {


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
        cont.await();
        log.info("完成发送！！！！");
    }
}