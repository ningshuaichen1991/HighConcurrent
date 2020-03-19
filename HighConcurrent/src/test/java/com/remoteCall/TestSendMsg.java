package com.remoteCall;

import com.ConcurrentPage.ConcurrentPageService;
import com.ConcurrentPage.pageCommon.Pagination;
import com.batchInsert.domain.Cost;
import com.sendMsg.SendMsgService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestSendMsg {

    @Resource
    SendMsgService sendMsgService;


    /**
     * 最简单直接的方式
     */
    @Test
    public void sendMsg() throws Exception {
        sendMsgService.sendServer("你好dfdsfdsfdsfdsfdsfdsfdsfdsfdsfdsfdsf");
    }
}