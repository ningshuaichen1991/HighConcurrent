package com;
import	java.util.concurrent.TimeUnit;

/**
 * 添加延时任务接口
 */
public interface IDelayedTask {
    /**
     * 添加任务
     * @param topic
     * @param key
     * @param times
     * @param timeUnit
     */
    void addDelayedTask(String topic, String key, long times, TimeUnit timeUnit);

    /**
     * 删除任务
     * @param topic
     * @param key
     */
    void removeDelayedTask(String topic, String key);
}
