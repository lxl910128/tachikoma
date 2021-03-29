package club.projectgaia.tachikoma.thread;


/**
 * @author Phoenix Luo
 * @version 2021/2/8
 **/
public class TestJoin {
    public static void main(String[] args) throws Exception {
        //join() 的作用：让“主线程”等待“子线程”结束之后才能继续运行。
        Thread t = new Thread(() -> {
            try {
                // 睡很长时间，主线程永不没法结束
                Thread.sleep(1000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("hello");
        });
        System.out.println("start");
        t.start();
        // 有join 输出 start hello end
        // 无join 输出 start end join
        t.join();
        System.out.println("end");
    }
}
