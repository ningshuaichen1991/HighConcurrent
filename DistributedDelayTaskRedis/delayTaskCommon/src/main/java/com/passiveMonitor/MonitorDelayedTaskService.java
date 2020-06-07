package com.passiveMonitor;

import com.IDelayedTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 添加被动监听的延时任务
 */
@Service("monitorDelayedTaskService")
@Slf4j
public class MonitorDelayedTaskService implements IDelayedTask {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void addDelayedTask(String topic, String key, long times, TimeUnit timeUnit) {
        boolean status =  stringRedisTemplate.opsForValue().setIfAbsent(topic+"_"+key,key,times, timeUnit);
        log.info("topic：{} , key：{} 添加延时任务{}",topic,key,status?"成功":"失败");
    }

    @Override
    public void removeDelayedTask(String topic, String key) {
        boolean status =  stringRedisTemplate.delete(topic+"_"+key);
        log.info("topic：{} , key：{} 移除延时任务{}",topic,key,status?"成功":"失败");
    }
}
