package com.remoteCall;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadPoolCommon {

    /**
     * corePool 为1 减少线程的切换
     * 最大线程数为cpu核数*2
     */

    private static ThreadPoolExecutor threadPoolExecutor = null;


    /**
     * 获取线程池对象
     * @param msg
     * @return
     */

    public static ThreadPoolExecutor getThreadPoolExecutor(String msg){
        if(threadPoolExecutor == null){
            synchronized (ThreadPoolExecutor.class){
                if(threadPoolExecutor == null){
                    threadPoolExecutor = new
                            ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() * 2,
                            60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(200), new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread t = new Thread(r);
                            t.setName("我的线程池-"+t.getName());
                            return t;
                        }
                    }, new MyRejected(msg));
                }
            }
        }
        return threadPoolExecutor;
    }

    /**
     * 重写拒绝策略
     */
    private static class MyRejected implements RejectedExecutionHandler {

        private String msg;

        public MyRejected(String msg){
            this.msg = msg;
        }

        /**
         * 最大努力执行任务型，当触发拒绝策略时，在尝试一分钟的时间重新将任务塞进任务队列，当一分钟超时还没成功时，就抛出异常
         */
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            try {
                log.info("msg：{}, 拒绝策略放进线程池中，60s中将继续尝试，如果超过60s则抛出异常",msg);
                threadPoolExecutor.getQueue().offer(r, 60, TimeUnit.SECONDS);//尝试60s中往队列中放入
            } catch (InterruptedException ex) {
               log.error("60s还是未成功放进线程池中,msg:"+msg,e);
            }
        }
    }

    private ThreadPoolCommon(){


    }
}