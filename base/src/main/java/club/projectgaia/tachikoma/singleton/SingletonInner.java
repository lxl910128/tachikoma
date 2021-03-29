package club.projectgaia.tachikoma.singleton;

/**
 * @author Phoenix Luo
 * @version 2021/3/3
 **/
public class SingletonInner {
    
    private SingletonInner() {
        System.out.println("init");
    }
    
    // 单例持有者
    // 内部类，私有，静态
    private static class InstanceHolder {
        private final static SingletonInner instance = new SingletonInner();
        
    }
    
    public static SingletonInner getInstance() {
        return InstanceHolder.instance;
    }
    
    public static void main(String[] args) {
        // start在init前 饿汉模式
        System.out.println("start");
        SingletonInner.getInstance();
    }
}
