package club.projectgaia.tachikoma;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Phoenix Luo
 * @version 2021/2/18
 **/
public class WaitAndNotify {
    
    public static void main(String[] args) {
        ReentrantLock rLock = new ReentrantLock();
        Condition condition = rLock.newCondition();
        Map<String, String> map = new HashMap();
        String lock = "aa";
        Thread a = new Thread(() -> {
            // synchronized (lock) {
            rLock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println(i * 2);
                    condition.signalAll();
                    condition.await();
                    // lock.notify();
                    // lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rLock.unlock();
            }
            //}
        });
        
        Thread b = new Thread(() -> {
            //  synchronized (lock) {
            rLock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println(i * 2 + 1);
                    condition.signalAll();
                    condition.await();
                    //lock.notify();
                    //lock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rLock.unlock();
            }
        });
        a.start();
        b.start();
    }
    
}
