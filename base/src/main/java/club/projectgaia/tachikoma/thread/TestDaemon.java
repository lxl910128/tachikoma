package club.projectgaia.tachikoma.thread;

/**
 * @author Phoenix Luo
 * @version 2021/2/9
 **/
public class TestDaemon {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            int i = 0;
            while (true) {
                i++;
                System.out.println("hello " + i);
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                }
            }
        });
        // 主线程不用等待守护线程结束再结束
        // 不设置 主线程永远不会结束
        t.setDaemon(true);
        t.start();
        Thread.sleep(1000L);
        System.out.println("bye！");
    }
}
