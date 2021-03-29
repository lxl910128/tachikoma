package club.projectgaia.tachikoma.thread;

import java.util.concurrent.CompletableFuture;

/**
 * @author Phoenix Luo
 * @version 2021/2/14
 **/
public class TestCompletableFuture {
    public static void main(String[] args) {
        // CompletableFuture 线程执行一个任务，可以直接定义他的完成后如何处理，失败时如何处理
        
        // 创建异步执行任务
        // 此时任务已提交执行
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(TestCompletableFuture::fetchPrice);
        // 如果执行成功:
        cf.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 如果执行异常:
        cf.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
        
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        
        }
        
        
        // 可以将多个 Completable串行
        // 第一个任务:
        CompletableFuture<String> cfQuery = CompletableFuture.supplyAsync(() -> {
            return "中国石油";
        });
        // cfQuery成功后继续执行下一个任务:
        CompletableFuture<Double> cfFetch = cfQuery.thenApplyAsync((code) -> {
            return Math.random();
        });
       
        // cfFetch成功后打印结果:
        cfFetch.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        
        }
        
       //Completable 可以各种方式编程任务
        // 任意1个任务完成
        CompletableFuture.anyOf();
        // 全部完成
        CompletableFuture.allOf();
        
    }
    
    static Double fetchPrice() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        if (Math.random() < 0.3) {
            throw new RuntimeException("fetch price failed!");
        }
        return 5 + Math.random() * 20;
    }
}
