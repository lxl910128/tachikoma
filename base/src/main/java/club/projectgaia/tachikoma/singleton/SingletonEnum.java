package club.projectgaia.tachikoma.singleton;

/**
 * @author Phoenix Luo
 * @version 2021/3/3
 * java推荐此种方法
 **/
public enum SingletonEnum {
    INSTANCE;
    // 资源，比如连接池之类的
    private String source;
    
    SingletonEnum() {
        // 写初始化资源的逻辑
        source = "jdbc";
        System.out.println("init");
    }
    
    public static void main(String[] args) {
        // init 在start 前，次方式为饿汉模式
        System.out.println("start");
        System.out.println(SingletonEnum.INSTANCE.source);
    }
}
