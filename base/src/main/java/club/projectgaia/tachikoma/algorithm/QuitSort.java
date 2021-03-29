package club.projectgaia.tachikoma.algorithm;

/**
 * @author Phoenix Luo
 * @version 2021/2/26
 **/
public class QuitSort {
    public static void main(String[] args) {
        int[] test = {6, 5, 4, 9, 8, 7, 3, 2, 1};
        quitSort(test, 0, test.length - 1);
        for (int i : test) {
            System.out.println("" + i);
        }
    }
    
    public static void quitSort(int[] test, int star, int end) {
        if (end <= star) {
            return;
        }
        // 作为中值
        int mid = test[end];
        //  下一大于中值的位置
        int nextBigger = star;
        // 找小于的 与下一个最大值交换
        for (int i = star; i < end; i++) {
            // 找到1个比mid小的，与nextBigger指向的大交换
            if (test[i] < mid) {
                change(test, i, nextBigger);
                // 向前进
                nextBigger++;
            }
        }
        change(test, end, nextBigger);
        
        // 下转
        quitSort(test, star, nextBigger - 1);
        quitSort(test, nextBigger + 1, end);
        
        
    }
    
    public static void change(int[] test, int first, int sec) {
        int tmp = test[first];
        test[first] = test[sec];
        test[sec] = tmp;
    }
}
