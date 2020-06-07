package com.loopPull;
import java.util.concurrent.*;
import	java.util.concurrent.locks.LockSupport;

import com.IDelayedTask;
import com.enums.BusinessTypeEnum;
import com.gateWay.DelayTaskBusinessGateWay;
import com.taskBusiness.IDelayedTaskBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * 循环拉取的方式监听
 */
@Component
@Slf4j
public class LoopPullDelayedTaskListener {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 导航具体业务实现类的网关
     */
    @Resource
    private DelayTaskBusinessGateWay delayTaskGateWay;

    /**
     * 用来添加或者删除延时任务的业务类
     */
    @Resource(name = "loopPullDelayedTaskService")
    private IDelayedTask loopPullDelayedTaskService;
    /**
     * 异步执行业务的线程池，咱们因为是做实验所以就用缓存的线程池
     */
    private static ExecutorService executors = Executors.newCachedThreadPool();
    /**
     * 各个模块延时任务的线程容器
     */
    private static Map<BusinessTypeEnum,Thread> redisDelayTaskThreadMap = new ConcurrentHashMap<>();
    /**
     * 空轮询数，空轮询数到了300那么线程将会被暂停
     */
    private final  int DEFAULT_EMPTY_POLL_COUNT = 10;


    /**
     * 根据业务类型获取线程对象
     * @param businessTypeEnum
     * @return
     */
    public Thread getLoopPullDelayedTaskThread(BusinessTypeEnum businessTypeEnum){
        return redisDelayTaskThreadMap.get(businessTypeEnum);
    }


    /**
     * 服务器启动后，启动各个业务的延时任务线程
     */
    @PostConstruct
    private  void runLoopPullDelayedTask(){
        List<BusinessTypeEnum> businessTypeEnumList = BusinessTypeEnum.getAllBusinessTypeList();//获取所有业务枚举对象
        for(BusinessTypeEnum businessTypeEnum:businessTypeEnumList){//循环遍历所有枚举对象，然后每一个枚举都对应着一个线程
            Thread thread = new Thread(new LoopPullDelayedTaskRunnable(businessTypeEnum));
            redisDelayTaskThreadMap.put(businessTypeEnum,thread);//把这些线程一个一个的添加到容器中
            thread.start();
        }
    }

    class LoopPullDelayedTaskRunnable implements Runnable {

        private BusinessTypeEnum businessTypeEnum;//业务类型

        volatile int loopCount;//轮询的次数

        LoopPullDelayedTaskRunnable(BusinessTypeEnum businessTypeEnum){
            this.businessTypeEnum = businessTypeEnum;
        }

        @Override
        public void run(){
            while (true){
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);//一秒钟轮询一次，可以按照自己设定
                } catch (InterruptedException e) {
                   log.error("InterruptedException：",e);
                }
                if(loopCount==DEFAULT_EMPTY_POLL_COUNT){//如果空轮询的次数超过了300次则wait
                    log.info("topic：{}开始进入等待阶段",businessTypeEnum.getBusinessValue());
                    LockSupport.park();//线程等待
                    loopCount=0;//如果有值或者被释放开则恢复初始值
                }
                BoundZSetOperations boundZSetOperations = stringRedisTemplate.boundZSetOps(businessTypeEnum.getBusinessValue());//获取zset集合对象
                log.info("boundZSetOperations.zCard：{}",boundZSetOperations.zCard());
                if(boundZSetOperations.zCard()==0){//如果是空集合则进行下一轮的循环，然后循环次数加1
                    log.info("topic：{}，还没有延时任务",businessTypeEnum.getBusinessValue());
                    loopCount++;
                    continue;
                }
                log.info("topic：{}，延时任务已经在监控中……",businessTypeEnum.getBusinessValue());
                loopCount=0;
                Set<ZSetOperations.TypedTuple> scoreSets =  boundZSetOperations.rangeWithScores(0,0);//获取第一个数据的set集合，其实是一个数据
                ZSetOperations.TypedTuple tuple = (ZSetOperations.TypedTuple) scoreSets.toArray()[0];//获取下标为0的TypedTuple对象
                double score = tuple.getScore();
                String value = (String)tuple.getValue();
                LocalDateTime localDateTime = LocalDateTime.now();
                long times = (localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
                if(times>=score){
                    log.info("已从zSet中取出，开始是执行topic："+businessTypeEnum.getBusinessValue()+"，value："+value);
                    IDelayedTaskBusiness business =  delayTaskGateWay.route(businessTypeEnum.getBusinessValue());
                    executors.execute(()->business.execute(businessTypeEnum.getBusinessValue(),value,loopPullDelayedTaskService));//异步执行任务防止堵塞主线程
                    boundZSetOperations.remove(value);
                }
            }
        }
    }
}