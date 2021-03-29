package club.projectgaia.tachikoma.algorithm;

/**
 * @author Phoenix Luo
 * @version 2021/2/28
 **/
public class Find {
    public static void main(String[] args) {
        Find find = new Find();
        System.out.println(find.numDecodings("10"));
    }
    
    public int numDecodings(String s) {
        
        char[] ss = s.toCharArray();
        
        if (ss.length == 0) {
            return 0;
        }
        if (ss[0] == '0') {
            return 0;
        }
        int[] ret = {0};
        find(ss, 0, ret);
        return ret[0];
    }
    
    public void find(char[] ss, int index, int[] ret) {
        if (index >= ss.length) {
            ret[0] = ret[0] + 1;
            return;
        }
        
        // 两位后有0，仅一种分法
        if (index + 2 < ss.length && ss[index + 2] == '0') {
            find(ss, index + 3, ret);
            return;
        }
        // 1位后有0，仅一种分法
        if (index + 1 < ss.length && ss[index + 1] == '0') {
            find(ss, index + 2, ret);
        }
        //此位是1，2个方法
        if (ss[index] == '1' && (index + 1) < ss.length) {
            find(ss, index + 1, ret);
            find(ss, index + 2, ret);
            return;
        }
        if (ss[index] == '2' &&
                (index + 1) < ss.length &&
                Integer.valueOf(ss[index + 1]) <= '6' ) {
            find(ss, index + 2, ret);
            find(ss, index + 1, ret);
            return;
        }
        find(ss, index + 1, ret);
    }
}
