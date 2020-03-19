package com.controller;

import com.batchInsert.service.CostService;
import com.concurrentExportExcel.service.ConCurrentExportExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequestMapping("test/")
public class TestController {

    @Resource
    private CostService costService;

    @Resource
    private ConCurrentExportExcel conCurrentExportExcel;

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
    public String exportCountCyclicBarrier(HttpServletResponse response){
        try{
            conCurrentExportExcel.exportCountCyclicBarrier(response);
        }catch (Exception e) {
            log.error("导出异常：",e);
        }
        return "success";
    }
}