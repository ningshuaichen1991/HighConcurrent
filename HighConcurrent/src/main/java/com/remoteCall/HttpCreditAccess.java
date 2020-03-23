package com.remoteCall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HttpCreditAccess {

    /**
     * 征信获取下载
     * @param idCode
     * @return
     */
    public String downloadCredit(String idCode){
        try {
            log.info("身份证号：{},开始远程拉取征信报告，线程号：{}",idCode,Thread.currentThread().getName());
            Thread.sleep(5000);
            log.info("拉取成功，并已存入磁盘中……");
            return "success";
        } catch (InterruptedException e) {
            log.error("拉取异常：",e);
        }
        return "downloadCreditError";
    }

    /**
     * 征信解析
     * @param idCode
     * @return
     */
    public String analyzeCredit(String idCode){
        try {
            log.info("身份证号：{},开始解析征信报告，线程号：{}",idCode,Thread.currentThread().getName());
            Thread.sleep(500);
            log.info("解析成功");
            return "success";
        } catch (InterruptedException e) {
            log.error("解析异常：",e);
        }
        return "analyzeCreditError";
    }
}