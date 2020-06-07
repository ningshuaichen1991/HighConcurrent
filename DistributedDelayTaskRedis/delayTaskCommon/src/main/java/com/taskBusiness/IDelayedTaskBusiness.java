package com.taskBusiness;

import com.IDelayedTask;

/**
 * 延时任务业务接口类
 */
public interface IDelayedTaskBusiness {
    /**
     * 任务执行调用此方法
     * @param topic 主题
     * @param key 关键字
     * @param delayedTask 添加任务的接口类
     * @return
     */
    boolean execute(String topic, String key, IDelayedTask delayedTask);
}
