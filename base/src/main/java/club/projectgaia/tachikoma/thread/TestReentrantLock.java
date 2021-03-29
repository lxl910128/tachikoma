package club.projectgaia.tachikoma.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Phoenix Luo
 * @version 2021/2/9
 **/
public class TestReentrantLock {
    // 可重入+公平锁
    // 可重入锁，锁内再获取锁
    private final Lock lock = new ReentrantLock();
    private int count;
    
    public static void main(String[] args) {
        // reentrantLock是可以代替 synchronized 的可重入锁
        TestReentrantLock testReentrantLock = new TestReentrantLock();
        //synchronized 是 可重入锁
        System.out.println(testReentrantLock.feibo(5));
        ExecutorService es = Executors.newFixedThreadPool(3);
        final TestReentrantLock reentrantLock = new TestReentrantLock();
        for (int i = 0; i < 3; i++) {
            int finalI = i;
            es.execute(() -> {
                reentrantLock.add(finalI, Thread.currentThread().getName());
            });
        }
        System.out.println("主线程结束");
        es.shutdown();
        
    }
    
    public void add(int n, String name) {
        // lock.lock() 等效于 add加synchronized
        System.out.println(System.currentTimeMillis() + "-" + name + "准备add" + n);
        lock.lock();
        try {
            count += n;
            // 如果是非重入锁，此处会死锁
            tryAdd(n, name);
            Thread.sleep(300);
        } catch (InterruptedException e) {
        
        } finally {
            lock.unlock();
            System.out.println(System.currentTimeMillis() + "-" + name + "add" + n + "完成");
        }
    }
    
    public void tryAdd(int n, String name) throws InterruptedException {
        // 尝试获取锁，并等待1分钟，1分钟内没获取则可以写其他逻辑 避免死锁
        if (lock.tryLock(1, TimeUnit.MINUTES)) {
            System.out.println(System.currentTimeMillis() + name + "准备tryAdd" + n);
            try {
                count += n;
            } finally {
                lock.unlock();
            }
        }
    }
    
    public synchronized int feibo(int n) {
        if (n == 1 || n == 2) {
            return 1;
        } else {
            // 如果synchronized是非可重入则会死锁
            return feibo(n - 1) + feibo(n - 2);
        }
    }
    
    // 自实现不可冲入锁
    public class undoLock {
        private boolean isLocked = false;
        
        public synchronized void lock() throws InterruptedException {
            while (isLocked) {
                wait();
            }
            isLocked = true;
        }
        
        public synchronized void unlock() {
            isLocked = false;
            notify();
        }
    }
    
    // 自实现可重入锁
    public class redoLock {
        boolean isLocked = false;
        Thread lockedBy = null;
        int lockedCount = 0;
        
        public synchronized void lock()
                throws InterruptedException {
            Thread thread = Thread.currentThread();
            while (isLocked && lockedBy != thread) {
                wait();
            }
            isLocked = true;
            lockedCount++;
            lockedBy = thread;
        }
        
        public synchronized void unlock() {
            if (Thread.currentThread() == this.lockedBy) {
                lockedCount--;
                if (lockedCount == 0) {
                    isLocked = false;
                    notify();
                }
            }
        }
    }
}
