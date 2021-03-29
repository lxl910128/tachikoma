package club.projectgaia.tachikoma.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Phoenix Luo
 * @version 2021/2/13
 **/
public class TestThreadPool {
    public static void main(String[] args) {
        // Executor 执行者
        // 执行线程的顶层接口 ，就一个execute方法，提交一个Runnable任务
        
        // ExecutorService
        // 继承 Executor
        // 多了提交任务的方式 submit 带返回值
        // 停止任务的方法 shutdown
        
        // ThreadFactory
        // 构建任务的接口，1个方法newThread 返回1个新线程
        
        // ThreadPoolExecutor
        // 线程池顶池 抽象类
        // 继承AbstractExecutorService， 其实现了ExecutorService，编写了相关方法的通用实现
        // 优秀文章https://blog.csdn.net/u011637069/article/details/79593114
        
        // 任务处理逻辑
        //1. 判断 corePoolSize 【创建】
        //2. 加入 workQueue
        //3. 判断 maximumPoolSize 【创建】 4. 执行拒绝策略处理器
        
        // 常见线程池
        // CachedThreadPool 无线空间的线程池(其实是 2^29次方)
        // 实质 new ThreadPoolExecutor(0, Integer.MAX_VALUE,60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>(),threadFactory)
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool((Runnable r) -> {
            Thread myThread = new Thread(r);
            myThread.setName("test");
            return myThread;
        });
        
        //newFixedThreadPool 定长线程池
        // 实质 new ThreadPoolExecutor(nThreads, nThreads,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(),threadFactory)
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10, (Runnable r) -> {
            Thread myThread = new Thread(r);
            myThread.setName("test");
            return myThread;
        });
        // 无限空间线程池，支持定时及周期性任务执行
        //
        ScheduledExecutorService scheduleThreadPool = Executors.newScheduledThreadPool(10, (Runnable r) -> {
            Thread myThread = new Thread(r);
            myThread.setName("test");
            return myThread;
        });
        // 10分钟后打印时间戳
        scheduleThreadPool.schedule(() -> System.out.println(System.currentTimeMillis()), 10, TimeUnit.MINUTES);
        // 1分钟后运行，每隔10分钟运行新线程 上个线程不完成，不执行
        scheduleThreadPool.scheduleAtFixedRate(() -> System.out.println(System.currentTimeMillis()), 1, 10, TimeUnit.MINUTES);
        // 1分钟后运行，每隔10分钟运行新线程
        scheduleThreadPool.scheduleWithFixedDelay(() -> System.out.println(System.currentTimeMillis()), 1, 10, TimeUnit.MINUTES);
        
        // SingleThreadExecutor
        // 单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
        // 本质
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor((Runnable r) -> {
            Thread myThread = new Thread(r);
            myThread.setName("test");
            return myThread;
        });
        
        
        // 自定义线程池 ThreadPoolExecutor
        /**
         * corePoolSize：(volatile int)类型，表示线程池核心池的大小。
         * maximumPoolSize：(volatile int)类型，表示线程池最多创建的线程数目。
         * 注：Java线程池分两部分，一块是核心池，一块是临时池，当核心池的线程满了且阻塞队列中任务满了，再有任务提交执行时，则创建"临时线程",可创建的临时线程的数目为maximumPoolSize-corePoolSize。当线程总数等于maximumPoolSize且阻塞队列满了，再有任务提交时，采取拒绝策略。
         *
         * workQueue：阻塞队列。常用的有ArrayBlockingQueue、LinkedBlockingQueue、PriorityBlockingQueue、DelayQueue。
         * 1. ArrayBlockingQueue:规定大小的 BlockingQueue，其构造必须指定大小。其所含 的对象是 FIFO 顺序排序的。
         * 2. LinkedBlockingQueue:大小不固定的 BlockingQueue，若其构造时指定大小，生 成的 BlockingQueue 有大小限制，不指定大小，其大小有 Integer.MAX_VALUE 来 决定。其所含的对象是 FIFO 顺序排序的。
         * 3. PriorityBlockingQueue:类似于 LinkedBlockingQueue，但是其所含对象的排序不 是 FIFO，而是依据对象的自然顺序或者构造函数的 Comparator 决定。
         * 4. SynchronizedQueue:特殊的 BlockingQueue，对其的操作必须是放和取交替完成。
         *
         * keepAliveTime：线程空闲回收时间。
         *
         * threadFactory：生成线程的工厂类。默认为：Executors.defaultThreadFactory()；
         *
         * handle：拒绝策略。默认为defaultHandler = new AbortPolicy()；
         *
         * 注：常用拒绝策略有以下几种：
         * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
         * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
         * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
         * ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
         */
        new ThreadPoolExecutor(3, 10,
                30, TimeUnit.MINUTES,
                //workQueue
                new LinkedBlockingQueue<Runnable>(),
                // threadFactory
                (Runnable r) -> {
                    Thread myThread = new Thread(r);
                    myThread.setName("test");
                    return myThread;
                },
                // 拒绝策略
                new ThreadPoolExecutor.AbortPolicy());
    }
    
}
