package com.remoteCall;

import com.sendMsg.SendMsgService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestSendMsg {

    @Resource
    SendMsgService sendMsgService;


    /**
     * 发送消息
     */
    @Test
    public void sendMsg() throws Exception {
        sendMsgService.sendServer("疫情是全世界的一场灾难");
    }
}