package club.projectgaia.tachikoma.thread;

import lombok.var;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Phoenix Luo
 * @version 2021/2/9
 * <p>
 * condition 来实现wait和notify的功能。
 * Condition提供的await()、signal()、signalAll()原理和
 * synchronized锁对象的wait()、notify()、notifyAll()是一致的
 **/
public class TestCondition {
    public static void main(String[] args) throws InterruptedException {
        new TestCondition().test();
    }
    
    public void test() throws InterruptedException {
        // 线程争抢的资源
        var q = new TestCondition.TaskQueue();
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
                            System.out.println("InterruptedException");
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
                    System.out.println("InterruptedException");
                }
            }
        });
        add.start();
        add.join();
        Thread.sleep(100);
        for (var t : ts) {
            // 中断阻塞线程
            // 中断 getTask的 wait
            t.interrupt();
            
        }
        
    }
    
    class TaskQueue {
        private final Lock lock = new ReentrantLock();
        // 必须从lock中获取condition
        private final Condition condition = lock.newCondition();
        private Queue<String> queue = new LinkedList<>();
        
        
        public void addTask(String s) {
            lock.lock();
            try {
                queue.add(s);
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
        
        public String getTask() throws InterruptedException {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    condition.await();
                }
                return queue.remove();
            } finally {
                lock.unlock();
            }
        }
    }
}
