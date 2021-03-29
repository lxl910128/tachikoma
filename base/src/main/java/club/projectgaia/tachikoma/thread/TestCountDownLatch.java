package club.projectgaia.tachikoma.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Phoenix Luo
 * @version 2021/2/14
 **/
public class TestCountDownLatch {
    public static void main(String[] args) {
        // 一个计数器类 使一个线程等待其他线程各自执行完毕后再执行
        // 实现方式：通过一个计数器来实现的，计数器的初始值是线程的数量。每当一个线程执行完毕后，计数器的值就-1，当计数器的值为0时，表示所有线程都执行完毕，然后在闭锁上等待的线程就可以恢复工作了
        // 主要功能：主线程等待 各个线程达到同一个状态后 主线程继续进行
        
        final CountDownLatch latch = new CountDownLatch(3);
        System.out.println("主线程开始执行…… ……");
        ExecutorService es = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            es.submit(() -> {
                System.out.println(finalI + " 号线程准备开始");
                try {
                    Thread.sleep((finalI + 1) * 100);
                    latch.countDown();
                    System.out.println(finalI + " 号countDown");
                    Thread.sleep((finalI + 1) * 100);
                    System.out.println(finalI + " 号线程结束");
                } catch (InterruptedException e) {
                
                }
            });
        }
        try {
            // 等待减到0
            latch.await();
        } catch (InterruptedException e) {
        
        }
        System.out.println("主线程结束");
        // 线程全部结束后 结束线程池，然后主线程才能结束
        es.shutdown();
        
        // countDown到0后 打印主线程结束
        // 然后陆续打印 x号线程结束
    }
}
