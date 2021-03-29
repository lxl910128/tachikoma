package club.projectgaia.tachikoma.algorithm;

/**
 * @author Phoenix Luo
 * @version 2021/3/9
 *
 * 百度
 * 链表逆序和枚举字符串所有排列
 **/
public class Baidu {
    public static void main(String[] args) {
        char[] a = new char[0];
        System.out.println(a.length);
        // 链表逆序
        
    }
    
    /**
     * node{
     * int value;
     * node next;
     * }
     *
     */
    public Node link(Node head) {
        // a-> b -> c-> null
        //
        
        //Node next = head.next;
        Node handle = head;
        Node last = null;
        while (handle != null) {
            Node tmp = handle.next;
            handle.next = last;
            last = handle;
            handle = tmp;
        }
        return last;
    }
    
    
    public static class Node {
        int value;
        Node next;
    }
    
    public void str(String str) {
        // abc
        // acd bac bca cab cba
        char[] chars = str.toCharArray();
        handle(new char[0], chars.length, new int[chars.length], chars);
    }
    
    // choose 0 表示没用过
    public void handle(char[] old, int length, int[] choose, char[] org) {
        if (old.length == length) {
            // 止
            System.out.println(new String(old));
        }
        for (int i = 0; i < choose.length; i++) {
            //
            if (choose[i] == 0) {
                choose[i] = 1;
                //
                char[] newChar = new char[old.length + 1];
                for (int j = 0; j < old.length; j++) {
                    newChar[j] = old[j];
                }
                newChar[old.length] = org[i];
                handle(newChar, length, choose, org);
                choose[i] = 0;
            }
        }
    }
}
