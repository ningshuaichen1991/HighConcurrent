package com.remoteCall;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;
import java.util.concurrent.*;

@Slf4j
public class ThreadPoolCommon {


    private ThreadPoolCommon(){}

    /**
     * 用來存放线程池出现的异常信息的，如果是正式环境可以用redis或者数据库代替
     */
    private static Queue<Runnable> threadPoolRunnableException = new ConcurrentLinkedQueue<>();
    /**
     * corePoolSize:
     * 每个任务需要tasktime秒处理，假如0.5秒，则每个线程每钞可处理1/0.5个任务。结合我们的系统,系统1秒有2个任务需要处理，则需要的线程数为：tasks/(1/tasktime)，
     * 即tasks*tasktime个线程数。假设系统每秒任务数为100～500，则需要100*0.5至200*0.5，即50～250个线程。那么corePoolSize应该设置为大于50，
     * 具体数字最好根据8020原则，即80%情况下系统每秒任务数，若系统80%的情况下任务数小于200，最多时为500，则corePoolSize可设置为100
     *
     * queueCapacity:
     * 任务队列的长度要根据核心线程数，以及系统对任务响应时间的要求有关。比如我们响应时间最多容忍1s，
     * 队列长度可以设置为(corePoolSize/tasktime)*responsetime： (100/0.5)*1=200，即队列长度可设置为200
     *
     * 队列长度设置过大，会导致任务响应时间过长，切忌以下写法：
     * LinkedBlockingQueue queue = new LinkedBlockingQueue();
     *
     * 这实际上是将队列长度设置为Integer.MAX_VALUE，将会导致线程数量永远为corePoolSize，再也不会增加，
     * 当任务数量陡增时，任务响应时间也将随之陡增
     *
     * maxPoolSize:
     * 当系统负载达到最大值时，核心线程数已无法按时处理完所有任务，这时就需要增加线程。每秒200个任务需要100个线程，那么当每秒达到500个任务时，
     * 则需要(500-queueCapacity)*(75/150)，即150个线程，可将maxPoolSize设置为150
     *
     * keepAliveTime:
     * 线程数量只增加不减少也不行。当负载降低时，可减少线程数量，如果一个线程空闲时间达到keepAliveTiime，该线程就退出。默认情况下线程池最少会保持corePoolSize个线程
     * 以上关于线程数量的计算并没有考虑CPU的情况。若结合CPU的情况，比如，当线程数量达到50时，CPU达到100%，则将maxPoolSize设置为60也不合适，
     * 此时若系统负载长时间维持在每秒1000个任务，
     * 则超出线程池处理能力，应设法降低每个任务的处理时间(tasktime)
     *
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