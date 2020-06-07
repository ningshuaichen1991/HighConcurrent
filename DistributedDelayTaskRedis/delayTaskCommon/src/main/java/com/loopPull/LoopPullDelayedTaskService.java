package com.loopPull;
import	java.time.LocalDateTime;

import com.IDelayedTask;
import com.enums.BusinessTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 添加循环拉取的任务数据
 */
@Service("loopPullDelayedTaskService")
@Slf4j
public class LoopPullDelayedTaskService implements IDelayedTask {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private LoopPullDelayedTaskListener loopPullDelayedTaskListener;


    @Override
    public void addDelayedTask(String topic, String key, long times, TimeUnit timeUnit) {
        BoundZSetOperations<String,String> zset = stringRedisTemplate.boundZSetOps(topic);
        LocalDateTime now = LocalDateTime.now();
        long time = 0L;
        if(timeUnit==TimeUnit.SECONDS){//秒
            time = now.plusSeconds(times).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }else if(timeUnit==TimeUnit.MINUTES){//分钟
            time=now.plusMinutes(times).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }else if(timeUnit==TimeUnit.HOURS){//小时
            time=now.plusHours(times).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }else if(timeUnit==TimeUnit.DAYS){//天
            time=now.plusDays(times).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        boolean status = zset.add(key,time);
        log.info("topic：{} , key：{} 添加延时任务{}",topic,key,status?"成功":"失败");
        if(!status){
            return;
        }
        Thread t = loopPullDelayedTaskListener.getLoopPullDelayedTaskThread(BusinessTypeEnum.getByValue(topic));//获取当前的topic线程如果waiting的状态则唤醒
        if(t.getState()== Thread.State.WAITING){
            LockSupport.unpark(t);
            log.info("topic：{} 线程已经从waiting中重新唤醒",topic);
        }
    }

    @Override
    public void removeDelayedTask(String topic, String key) {
        BoundZSetOperations<String,String> zset = stringRedisTemplate.boundZSetOps(topic);
        long count=zset.remove(key);
        log.info("topic：{} , key：{} 移除延时任务{}",topic,key,count>0?"成功":"失败");
    }
}