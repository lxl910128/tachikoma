package club.projectgaia.tachikoma.thread;

import java.util.concurrent.locks.StampedLock;

/**
 * @author Phoenix Luo
 * @version 2021/2/13
 **/
public class TestStampedLock {
    public static void main(String[] args) {
        new TestStampedLock().test();
    }
    
    public void test() {
        Point point = new Point();
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                point.move(this);
            }
        };
        t.start();
        for (int i = 0; i < 10; i++) {
            new Thread("read-" + i) {
                @Override
                public void run() {
                    super.run();
                    point.distanceFromOrigin(this.getName());
                }
            }.start();
        }
        // 等待 读线程逻辑完成
        try {
            Thread.sleep(1200L);
        } catch (InterruptedException e) {
        
        }
        // 中断写
        t.interrupt();
        
        // 首先打印各个线程 乐观锁获取 的xy，此时10个线程 彼此间xy不相等且 每个线程的xy值也不等
        // 然后打印各个线程 获取悲观锁
        // 并打印悲观锁下 xy的值，此时各线程中的xy值是相等的，证明悲观读锁 锁住了写操作
        // 同时线程之间的xy值也是相同的，证明读锁可以同时被多个线程获取
        
    }
    
    public class Point {
        private final StampedLock stampedLock = new StampedLock();
        private long x;
        private long y;
        
        public void move(Thread t) {
            while (!t.isInterrupted()) {
                long stamp = stampedLock.writeLock(); // 获取写锁
                try {
                    x++;
                    y++;
                } finally {
                    stampedLock.unlockWrite(stamp); // 释放写锁
                }
            }
        }
        
        public void distanceFromOrigin(String name) {
            // 获得一个乐观读锁
            // 乐观读锁 不锁 读也不锁写
            long stamp = stampedLock.tryOptimisticRead();
            // 注意下面两行代码不是原子操作
            // 假设x,y = (100,200)
            double currentX = x;
            // 此处已读取到x=100，但x,y可能被写线程修改为(300,400)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            
            }
            double currentY = y;
            // 此处已读取到y，如果没有写入，读取是正确的(100,200)
            // 如果有写入，读取是错误的(100,400)
            System.out.println(name + "乐观读获取的 x=" + currentX + " ，y=" + currentY);
            if (!stampedLock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生
                // 加悲观锁，锁写操作，还是不锁读
                stamp = stampedLock.readLock(); // 获取一个悲观读锁
                System.out.println(name + "加悲观读锁");
                try {
                    currentX = x;
                    currentY = y;
                    System.out.println(name + "悲观读锁获取的 x=" + currentX + " ，y=" + currentY);
                } finally {
                    stampedLock.unlockRead(stamp); // 释放悲观读锁
                }
            }
        }
    }
}
