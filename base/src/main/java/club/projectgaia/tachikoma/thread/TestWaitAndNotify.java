package club.projectgaia.tachikoma.thread;

import lombok.var;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Phoenix Luo
 * @version 2021/2/9
 **/
public class TestWaitAndNotify {
    public static void main(String[] args) throws InterruptedException {
        new TestWaitAndNotify().test();
    }
    
    public void test() throws InterruptedException {
        // 线程争抢的资源
        var q = new TaskQueue();
        // 已创建的线程
        var ts = new ArrayList<Thread>();
        // 5个线程等获取Task
        for (int i = 0; i < 5; i++) {
            var t = new Thread() {
                @Override
                public void run() {
                    // 执行task:
                    while (true) {
                        try {
                            String s = q.getTask();
                            System.out.println(this.getName() + " execute task: " + s);
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            };
            t.setName(i + "-Thread");
            t.start();
            ts.add(t);
        }
        // 1个线程放Task
        var add = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                // 放入task:
                String s = "t-" + Math.random();
                System.out.println("add task: " + s);
                q.addTask(s);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // 中断报错在此捕获不做处理
                }
            }
        });
        add.start();
        add.join();
        Thread.sleep(100);
        for (var t : ts) {
            // 中断阻塞线程
            // 中断 getTask的 wait ,会报错
            t.interrupt();
        }
        
    }
    // 注意 notify 和 wite都是Object的方法
    // 注意使用时 是你锁定的资源调用这些方法
    class TaskQueue {
        Queue<String> queue = new LinkedList<>();
        // 锁方法，相当于锁 this
        public synchronized void addTask(String s) {
            this.queue.add(s);
            // 通知等待状态的线程
            this.notifyAll();
        }
        
        // 锁方法，相当于锁 this
        // 与addTask 争抢同一把锁
        public synchronized String getTask() throws InterruptedException {
            while (queue.isEmpty()) {
                // 线程进入 等待状态，同时会释放本线程持有的锁
                this.wait();
            }
            return queue.remove();
        }
    }
}
