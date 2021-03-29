package club.projectgaia.tachikoma.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Phoenix Luo
 * @version 2021/2/13
 **/
public class TestReadWriteLock {
    public static void main(String[] args) {
        new TestReadWriteLock().test();
    }
    
    public void test() {
        
        
        Counter counter = new Counter();
        // 3个写
        for (int i = 0; i < 10; i++) {
            new Thread("write-" + i) {
                @Override
                public void run() {
                    super.run();
                    counter.inc(this.getName());
                }
            }.start();
        }
        // 5个读
        for (int i = 0; i < 5; i++) {
            new Thread("read-" + i) {
                @Override
                public void run() {
                    super.run();
                    counter.get(this.getName());
                }
            }.start();
        }
        
        // 首先打印各个线程开始
        // 然后循环打印3遍 " write 获取写锁  write 释放写锁" 证明只有一个线程持有写锁，写锁也会阻塞读锁
        // 然后连续打印 5遍 "获取读锁" 再连续打印 5遍"释放读锁" 证明 读锁可以多线性共同持有
        // 读锁也会阻塞 写锁
        // 注意，顺序创建的线程，获取锁的优先级也是顺序的，所以永远是先3个写完了再是5个读，没有出现读在写前出现的情况
        
    }
    
    
    public class Counter {
        private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
        private final Lock rlock = rwlock.readLock();
        private final Lock wlock = rwlock.writeLock();
        private int counts = 0;
        
        
        public void inc(String name) {
            System.out.println(name + "开始");
            wlock.lock(); // 加写锁
            try {
                System.out.println(name + "获取写锁" + (counts++));
            } finally {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException E) {
                
                }
                wlock.unlock(); // 释放写锁
                System.out.println(name + "释放写锁");
            }
        }
        
        public void get(String name) {
            System.out.println(name + "开始");
            rlock.lock(); // 加读锁
            try {
                System.out.println(name + "获取读锁" + counts);
            } finally {
                //  释放锁前先sleep
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException E) {
                
                }
                rlock.unlock(); // 释放读锁
                System.out.println(name + "释放读锁");
            }
        }
    }
}
