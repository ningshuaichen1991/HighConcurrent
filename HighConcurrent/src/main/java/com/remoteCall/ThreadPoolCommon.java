package com.remoteCall;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.*;

@Slf4j
public class ThreadPoolCommon {


    private ThreadPoolCommon(){}

    /**
     * 用來存放线程池出现的异常信息的，如果是正式环境可以用redis或者中间件代替
     */
    private static Queue<Runnable> threadPoolRunnableException = new ConcurrentLinkedQueue<>();
    /**
     *
     *线程池实例化
     */
    private static ThreadPoolExecutor threadPoolExecutor =  new
            ThreadPoolExecutor(100, 150,
            60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("我的线程池-"+t.getName());
            return t;
        }
    }, new MyRejected());


    /**
     * 获取线程池对象
     * @return
     */
    public static ThreadPoolExecutor getThreadPoolExecutor(){
        return threadPoolExecutor;
    }

    /**
     * 重写拒绝策略
     */
    private static class MyRejected implements RejectedExecutionHandler {

        /**
         * 最大努力执行任务型，当触发拒绝策略时，在尝试一分钟的时间重新将任务塞进任务队列，
         * 当一分钟超时还没成功时，就抛出异常
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            try {
                log.info("msg：{}, 拒绝策略放进线程池中，60s中将继续尝试，如果超过60s则抛出异常");
                threadPoolExecutor.getQueue().offer(r, 60, TimeUnit.SECONDS);//尝试60s中往队列中放入
            } catch (InterruptedException ex) {
                log.error("60s还是未成功放进线程池中",e);
                threadPoolRunnableException.add(r);
            }
        }
    }

}