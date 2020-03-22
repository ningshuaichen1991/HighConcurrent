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
     * 不需要返回值的多线程设计
     */
    @Test
    public void testEasiestWay() throws Exception {
        loanService.addLenderInformationEasiestWay("1221212121");
    }

    /**
     *需要返回值的多线程远程调用
     */
    @Test
    public void testFuture() throws Exception {
        loanService.addLenderInformationFuture("565656");
    }


    /**
     *通过join的方式实现
     */
    @Test
    public void testJoin() throws Exception {
        loanService.addLenderInformationJoin("66666");
    }


    /**
     * 征信下载和征信解析
     */
    @Test
    public void testCompletableFuture() throws Exception {
        loanService.addLenderInformationCompletableFuture("66666");
    }
}