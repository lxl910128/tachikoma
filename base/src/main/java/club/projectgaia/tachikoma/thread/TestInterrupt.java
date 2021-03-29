package club.projectgaia.tachikoma.thread;

/**
 * @author Phoenix Luo
 * @version 2021/2/8
 **/
public class TestInterrupt {
    
    public static void main(String[] args) throws InterruptedException {
        // 中断，停止线程运行
        // A线程调用B线程中断，就是给B发个信号让其停止，但 是否接受中断得看B逻辑
        // 打印N个 hello后退出
        Thread t = new Thread() {
            @Override
            public void run() {
                int n = 0;
                // 随时关注 是否被中断
                // 不写则线程一直不退出
                while (!isInterrupted()) {
                    n++;
                    System.out.println(n + " hello!");
                }
            }
        };
        t.start();
        Thread.sleep(1); // 暂停1毫秒
        t.interrupt(); // 中断t线程
        t.join(); // 等待t线程结束
        System.out.println("end");
        
        // 使用volatile 达到相同效果
        HelloThread p = new HelloThread();
        
        p.start();
        Thread.sleep(1);
        p.running = false;
        p.join();
        System.out.println("end");
        
        // 线程处于等待状态，中断会报错InterruptedException
        // 也就是 sleep需要抛出的异常
        Thread s = new Thread() {
            @Override
            public void run() {
                int n = 0;
                while (!isInterrupted()) {
                    n++;
                    System.out.println(n + " hello!  ss");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupted");
                        break;
                    }
                }
            }
        };
        s.start();
        Thread.sleep(1000L);
        // 中断等待线程会报错
        s.interrupt();
        
    }
    
}

// 等效 中断
// 不能使用匿名方式创建,Thread找不到 running变量
// 使用volatile关键字标记，确保每个线程都能读取到更新后的变量值
class HelloThread extends Thread {
    public volatile boolean running = true;
    
    @Override
    public void run() {
        int n = 0;
        while (running) {
            n++;
            System.out.println(n + " hello word!");
        }
    }
}