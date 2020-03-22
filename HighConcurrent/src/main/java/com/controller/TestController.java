package com.controller;

import com.ConcurrentPage.ConcurrentPageService;
import com.ConcurrentPage.pageCommon.Pagination;
import com.batchInsert.domain.Cost;
import com.batchInsert.service.CostService;
import com.concurrentExportExcel.service.ConCurrentExportExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("test/")
public class TestController {

    @Resource
    private CostService costService;

    @Resource
    private ConCurrentExportExcel conCurrentExportExcel;

    @Resource
    private ConcurrentPageService concurrentPage;

    /**
     * 多线程批量插入
     * @return
     */
    @RequestMapping("insertBatchForkin")
    @ResponseBody
    public String insert(){
        costService.insertBatchForkin();
        return "success";
    }



    /**
     * 导出
     * @return
     */
    @RequestMapping("conCurrentExportExcel")
    @ResponseBody
    public String conCurrentExportExcel(HttpServletResponse response){
        try{
            conCurrentExportExcel.exportCountDownLatch(response);
        }catch (Exception e) {
            log.error("导出异常：",e);
        }
        return "success";
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping("exportCountCyclicBarrier")
    @ResponseBody
    public String exportCountCyclicBarrier(HttpServletResponse response, HttpServletRequest request){
        try{
            conCurrentExportExcel.exportCountCyclicBarrier(request,response);
        }catch (Exception e) {
            log.error("导出异常：",e);
        }
        return "success";
    }


    /**
     * 导出
     * @return
     */
    @RequestMapping("queryConcurrentCostPage")
    @ResponseBody
    public String queryConcurrentCostPage(HttpServletResponse response, HttpServletRequest request){
        try{
            Map<String,Object> params = new HashMap<>();
            params.put("type","主动还款");
            params.put("startNo",1);
            params.put("pageSize",1000);
            long s = System.currentTimeMillis();
            Pagination<Cost> costPagination =  concurrentPage.queryConcurrentCostPage(params);
            log.info("总共消耗时间：{}",(System.currentTimeMillis() - s));
        }catch (Exception e) {
            log.error("导出异常：",e);
        }
        return "success";
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping("querySerialCostPage")
    @ResponseBody
    public String querySerialCostPage(HttpServletResponse response, HttpServletRequest request){
        try{
            Map<String,Object> params = new HashMap<>();
            params.put("type","主动还款");
            params.put("startNo",1);
            params.put("pageSize",1000);
            long s = System.currentTimeMillis();
            Pagination<Cost> costPagination =  concurrentPage.querySerialCostPage(params);
            log.info("总共消耗时间：{}",(System.currentTimeMillis() - s));
        }catch (Exception e) {
            log.error("导出异常：",e);
        }
        return "success";
    }

}