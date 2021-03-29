package club.projectgaia.tachikoma.thread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author Phoenix Luo
 * @version 2021/2/14
 **/
public class TestCyclicBarrier {
    public static void main(String[] args) {
        // CyclicBarrier让所有线程都等待完成后才会继续下一步行动。
        // 所有线程都准备好再进行后续工作
        CyclicBarrier barrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + " 开始最后任务" + System.currentTimeMillis());
                    Thread.sleep(1000L);
                    System.out.println(Thread.currentThread().getName() + " 完成最后任务" + System.currentTimeMillis());
                } catch (InterruptedException e) {
                
                }
            }
        });
        ExecutorService es = Executors.newFixedThreadPool(5, new ThreadFactory() {
            private Integer i = 0;
            
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "MyThread-" + (i++));
            }
        });
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            es.execute(new Runnable() {
                @Override
                public void run() {
                    {
                        try {
                            Thread.sleep((finalI + 1) * 100);
                            System.out.println(Thread.currentThread().getName() + " 到达栅栏 " + System.currentTimeMillis());
                            barrier.await();
                            System.out.println(Thread.currentThread().getName() + " 冲破栅栏 " + System.currentTimeMillis());
                        } catch (InterruptedException | BrokenBarrierException e) {
                        
                        }
                    }
                }
            });
        }
        System.out.println("主线程结束");
        es.shutdown();
        
        // 观察可以发现 5个线程叨叨栅栏的是回见略有不同
        // 但冲出栅栏的时间是几乎一样的
        // 最后到达的线程 先执行 栅栏定义的最后执行任务，
        // 执行完后再打开栅栏执行各线程后续操作
    }
}
