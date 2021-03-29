package club.projectgaia.tachikoma.singleton;

/**
 * @author Phoenix Luo
 * @version 2021/3/3
 **/
public class SingletonHungry {
    private static SingletonHungry instance = new SingletonHungry();
    
    private SingletonHungry() {
    
    }
    
    public static SingletonHungry getInstance() {
        // 不能this. 因为在静态方法中
        return instance;
    }
    
    
}
