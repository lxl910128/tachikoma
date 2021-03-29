package club.projectgaia.tachikoma.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Phoenix Luo
 * @version 2021/2/14
 **/
public class TestCallable {
    public static void main(String[] args) {
        // Callable 与 Runnable类似 但callable有返回值
        // callable 提交到线程池后得到Future
        // future.get 阻塞当前线程来取回线程执行结果，可设置超时
        Callable<Long> callable = () -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            
            }
            return System.currentTimeMillis();
        };
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Future<Long> future = pool.submit(callable);
        try {
            //System.out.println(future.get(5, TimeUnit.SECONDS));
            System.out.println(future.isDone());// 返回线程是否完成
            System.out.println(future.get(1, TimeUnit.SECONDS));
        } catch (TimeoutException timeOut) {
            System.out.println("time out");
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getLocalizedMessage());
        } finally {
            // 线程池不退出
            // main函数不退出
            pool.shutdown();
        }
    }
}
