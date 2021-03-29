package club.projectgaia.tachikoma;

/**
 * @author Phoenix Luo
 * @version 2021/3/29
 * <p>
 * 内部类 和 静态内部类的区别
 **/
public class InnerClassTest {
    private int value;
    
    private void future() {
        System.out.println("外部类 普通方法");
    }
    
    public static void staticFuture() {
        System.out.println("外部类 静态方法");
    }
    
    public void innerFuture() {
        new InnerClass().innerFuture();
    }
    
    // 内部类
    // 隐藏在外部类中，封装性更强，不允许外部类的其他类访问它，但他可以直接访问外部类的成员
    // 他的核心意义是 可以将 仅仅此类（InnerClassTest）用到的类（InnerClass）封装到 该类中， 保持代码整洁
    // 试想如果 不这样做，HashMap 没必要暴露的 Entry等类就需要单独提一个类出来，还破坏封装性
    private class InnerClass {
        
        private void innerFuture() {
            //非静态内部类由于持有对外部类的引用，可以访问外部类的所用成员
            System.out.println("内部类 普通方法");
            future();
            value = 3;
        }
    }
    
    // 内部静态类
    static class StaticInnerClass {
        private void staticInnerFuture() {
            // 不能调用外部类 非静态方法
            //  future();
            // 静态内部类只能访问外部类的静态成员变量和静态方法，
            staticFuture();
        }
    }
    
    public static void main(String[] args) {
        new InnerClassTest().future();
        // 此处错误
        // 而非静态内部类需要通过生成外部类来间接生成
        // new InnerClass().innerFuture();
        
        //静态内部类是指被声明为static的内部类，可不依赖外部类实例化
        new StaticInnerClass().staticInnerFuture();
        
    }
}
