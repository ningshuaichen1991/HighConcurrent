package com.remoteCall;
import java.util.concurrent.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;

@Service
@Slf4j
public class LoanService {

    @Resource
    private HttpCreditAccess httpCreditAccess;

    /**
     * 最简单的方式不管成功还是失败
     * @param idCode
     * @throws Exception
     */
    public void addLenderInformationEasiestWay(String idCode) throws Exception{
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolCommon.getThreadPoolExecutor(idCode);
        long s = System.currentTimeMillis();
        log.info("借款人身份证号：{} 信息开始保存……",idCode);
        log.info("保存中，正在入库个人的信息");
        threadPoolExecutor.execute(()->httpCreditAccess.downloadCredit(idCode));
        Thread.sleep(1000);
        log.info("所有数据入口、征信都已处理完成！");
        log.info("总耗时：{}",System.currentTimeMillis() -s);
        Thread.sleep(5000);
    }

    /**
     * 直到运行成功
     * @param idCode
     * @throws Exception
     */
    public void addLenderInformationFuture(String idCode) throws Exception{
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolCommon.getThreadPoolExecutor(idCode);
        long s = System.currentTimeMillis();
        log.info("借款人身份证号：{} 信息开始保存……",idCode);
        log.info("保存中，正在入库个人的信息");
        Future <String> future = threadPoolExecutor.submit(new Callable<String>(){
            @Override
            public String call() throws Exception {
                return httpCreditAccess.downloadCredit(idCode);
            }
        });
        Thread.sleep(1000);
        String downLoadStatus= future.get(1,TimeUnit.SECONDS);//一秒钟如果没有得到结果则超时
        log.info("下载征信状态：{}",downLoadStatus);
        log.info("所有数据入口、征信都已处理完成！");
        log.info("总耗时：{}",System.currentTimeMillis() -s);
    }


    /**
     * 最简单的方式不管成功还是失败
     * 出现异常了无法捕获
     * @param idCode
     * @throws Exception
     */
    public void addLenderInformationJoin(String idCode) throws Exception{
        long s = System.currentTimeMillis();
        log.info("借款人身份证号：{} 信息开始保存……",idCode);
        log.info("保存中，正在入库个人的信息");
        Thread thread = new Thread(()->httpCreditAccess.downloadCredit(idCode));
        thread.start();
        Thread.sleep(1000);
        thread.join();
        log.info("所有数据入口、征信都已处理完成！");
        log.info("总耗时：{}",System.currentTimeMillis() -s);
    }


    /**
     * 最简单的方式不管成功还是失败
     * @param idCode
     * @throws Exception
     */
    public void addLenderInformationCompletableFuture(String idCode) throws Exception{
        long s = System.currentTimeMillis();
        log.info("借款人身份证号：{} 信息开始保存……",idCode);
        log.info("保存中，正在入库个人的信息");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> httpCreditAccess.downloadCredit(idCode))
                .thenApply(status -> "success".equals(status)?httpCreditAccess.analyzeCredit(idCode):status);
        Thread.sleep(1000);
        String status = future.get(2000,TimeUnit.SECONDS);
        if(!"success".equals(status)){
            log.info("下载或者解析出现异常：{}",status);
            return;
        }
        log.info("所有数据入口、征信都已处理完成！");
        log.info("总耗时：{}",System.currentTimeMillis() -s);
    }
}