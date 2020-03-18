package com.remoteCall;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestHighConcurrentRemoteCall {

    @Resource
    LoanService loanService;


    /**
     * 最简单直接的方式
     */
    @Test
    public void testEasiestWay() throws Exception {
        loanService.addLenderInformationEasiestWay("1221212121");
    }

    /**
     *
     */
    @Test
    public void testFuture() throws Exception {
        loanService.addLenderInformationFuture("565656");
    }


    /**
     *
     */
    @Test
    public void testJoin() throws Exception {
        loanService.addLenderInformationJoin("66666");
    }


    /**
     *
     */
    @Test
    public void testCompletableFuture() throws Exception {
        loanService.addLenderInformationCompletableFuture("66666");
    }
}