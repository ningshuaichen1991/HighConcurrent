package com.batchInsert.service;
import java.io.File;
import	java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import	java.util.Random;

import com.batchInsert.dao.CostDao;
import com.batchInsert.domain.Cost;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

@Service
@Slf4j
public class CostService {

    @Resource
    private CostDao costDao;

    public void insertBatchForkin(){
        List<Cost> costList = getAllCost();
        ForkJoinPool pool=new ForkJoinPool();
        pool.execute(new MyTask(costList, 0, costList.size()));
        pool.shutdown();
    }

    public void select(){
        getAllCost();
    }

    class MyTask extends RecursiveAction {

        private List<Cost> costList;

        private static final int THRESHOLD =1000;//最多1000个为一组insert

        private int start;//从list中哪一个位置开始

        private int end;//从list中哪一个位置结束

        public MyTask(List<Cost> costList,int start, int end) {
            this.costList = costList;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute(){
            if(end - start < THRESHOLD){
                List<Cost> costListInsert = costList.subList(start, end);
                costDao.batchInsert(costListInsert);
                log.info("插入完成："+costListInsert.size());
            }else{
                // 如果当end与start之间的差大于THRESHOLD时，即要插入的数超过1000个
                // 将大任务分解成两个小任务。
                int middle = (start + end) /2;
                MyTask left = new MyTask(costList,start, middle);
                MyTask right = new MyTask(costList,middle, end);
                // 并行执行两个“小任务”
                left.fork();
                right.fork();
                log.info("分完任务，start ："+start+",end:"+end);
            }
        }
    }

    /**
     * 模拟将要插入的费用数据
     * @return
     */
    private List<Cost> getAllCost(){
        String typeArray [] = {"划扣","主动还款","逾期还款"};
        Random random = new Random();
        List<Cost> costList = Lists.newArrayList();
        for(int i = 1;i<=10000000;i++){
            costList.add(new Cost(i,typeArray[random.nextInt(3)],new Date(), BigDecimal.valueOf(i)));
        }
        return costList;
    }
}