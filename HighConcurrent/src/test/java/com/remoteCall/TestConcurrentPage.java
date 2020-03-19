package com.remoteCall;

import com.ConcurrentPage.pageCommon.Pagination;
import com.ConcurrentPage.ConcurrentPageService;
import com.batchInsert.domain.Cost;
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
public class TestConcurrentPage {

    @Resource
    ConcurrentPageService concurrentPage;


    /**
     * 最简单直接的方式
     */
    @Test
    public void testEasiestWay() throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("type","主动还款");
        params.put("startNo",1);
        params.put("pageSize",100);
        Pagination<Cost>  costPagination =  concurrentPage.queryCostPage(params);
    }
}