package club.projectgaia.tachikoma.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author Phoenix Luo
 * @version 2021/2/14
 **/
public class TestSemaphore {
    private static final int THREAD_COUNT = 30;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
    private static Semaphore s = new Semaphore(10);
    
    public static void main(String[] args) {
        // Semaphore 核心功能是限流
        // 初始化时设置有几个信标
        // 线程抢到信标才能执行，否则阻塞
        // 线程池可以通过线程池大小来限制并发
        // Semaphore的特点是可以通过定义不同线程所需信标多少来调整线程的优先级
        for (int i = 0; i < 5; i++) {
            int finalI = i + 1;
            threadPool.execute(() -> {
                try {
                    System.out.println(finalI + "号准备获取信标" + s.availablePermits());
                    // 不能不打断
                    s.acquireUninterruptibly(finalI);
                    System.out.println(finalI + "号获取信标" + finalI + "个");
                    Thread.sleep((8 - finalI) * 1000);
                    // 能被打断 ，打断抛出错误
                    //s.acquire();
                } catch (InterruptedException e) {
                } finally {
                    s.release(finalI);
                    System.out.println(finalI + "号释放信标" + s.availablePermits());
                }
                
            });
        }
        System.out.println("主线程结束");
        threadPool.shutdown();
        // 通过实验可以发现，5个线程总共需要15个信标，虽然开了5个线程但是肯定有线程在获取信标时被阻塞
        // 当其他线程释放了信标才能继续执行
    }
}
