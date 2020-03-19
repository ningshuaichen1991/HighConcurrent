package com.remoteCall;

import com.batchInsert.service.CostService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestBatchInsert {

    @Resource
    CostService costService;


    /**
     * 最简单直接的方式
     */
    @Test
    public void testEasiestWay() throws Exception {
        costService.insertBatchForkin();
    }

    @Test
    public void testSelect() throws Exception {
        costService.select();
    }
}