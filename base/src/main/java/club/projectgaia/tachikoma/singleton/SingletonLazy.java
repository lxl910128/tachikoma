package club.projectgaia.tachikoma.singleton;

/**
 * @author Phoenix Luo
 * @version 2021/3/3
 **/
public class SingletonLazy {
    // 此处必须是static 因为 static方法中要使用
    // 保证并发时的可见性  避免重排序问题导致其他的线程看到了一个已经分配内存和地址但没有初始化的对象
    private volatile static SingletonLazy instance;
    
    private static Object lock = new Object();
    
    
    private SingletonLazy() {
    
    }
    
    // 方法上加 synchronized 关键字的话性能会略有损耗
    public static SingletonLazy getInstance() {
        if (instance == null) {
            // 因为在静态方法中所以不能锁this
            // 不能锁instance 因为此刻他还是null，会空指针异常
            // 可以锁这个类，也可以锁一个static 的对象
            //synchronized (SingletonLazy.class) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new SingletonLazy();
                }
            }
        }
        return instance;
    }
    
    public static void main(String[] args) {
        SingletonLazy.getInstance();
    }
    
}
