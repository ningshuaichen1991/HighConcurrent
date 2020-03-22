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
     * 并行执行测试
     */
    @Test
    public void queryConcurrentCostPage() throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("type","主动还款");
        params.put("startNo",1);
        params.put("pageSize",100);
        long s = System.currentTimeMillis();
        Pagination<Cost>  costPagination =  concurrentPage.queryConcurrentCostPage(params);
        log.info("总共消耗时间：{}",(System.currentTimeMillis() - s));
    }

    /**
     * 串行执行测试  15071
     */
    @Test
    public void querySerialCostPage() throws Exception {
        Map<String,Object> params = new HashMap<>();
        params.put("type","主动还款");
        params.put("startNo",1);
        params.put("pageSize",100);
        long s = System.currentTimeMillis();
        Pagination<Cost>  costPagination =  concurrentPage.querySerialCostPage(params);
        log.info("总共消耗时间：{}",(System.currentTimeMillis() - s));
    }
}