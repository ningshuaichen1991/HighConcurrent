package com.passiveMonitor;

import com.IDelayedTask;
import com.enums.BusinessTypeEnum;
import com.gateWay.DelayTaskBusinessGateWay;
import com.taskBusiness.IDelayedTaskBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 被动监听的方式进行延时任务的处理
 */
@Component
@Slf4j
public class MonitorDelayedTaskListener extends KeyExpirationEventMessageListener {

    @Resource
    private DelayTaskBusinessGateWay delayTaskBusinessGateWay;

    @Resource(name="monitorDelayedTaskService")
    private IDelayedTask monitorDelayedTaskService;

    private static ExecutorService executors = Executors.newCachedThreadPool();//执行延时任务逻辑的线程池


    public MonitorDelayedTaskListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * 针对redis数据失效事件，进行数据处理
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
        if(!expiredKey.contains("_")){
            return;
        }
        //设置监听频道
        String keyArray[]  = expiredKey.split("_");
        if(!BusinessTypeEnum.getAllBusinessTypeStringList().contains(keyArray[0])){
            return;
        }
        log.info("topic：{},key：{}开始执行延时任务……",keyArray[0],keyArray[1]);
        IDelayedTaskBusiness business =  delayTaskBusinessGateWay.route(keyArray[0]);
        executors.execute(()->business.execute(keyArray[0],keyArray[1],monitorDelayedTaskService));
    }
}