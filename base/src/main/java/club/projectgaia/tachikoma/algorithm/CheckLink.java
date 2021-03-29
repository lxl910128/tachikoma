package club.projectgaia.tachikoma.algorithm;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Phoenix Luo
 * @version 2021/3/16
 * 判断一个链表是否有环
 **/
public class CheckLink {
    public static void main(String[] args) {
        //
    }
    
    // false 表示有环
    // true 表示无环
    // 第一种方法
    public boolean check1(Link header) {
        Set<Link> checked = new HashSet<>();
        Link handler = header;
        while (handler != null) {
            if (checked.contains(handler)) {
                return false;
            }
            checked.add(handler);
            handler = header.child;
        }
        return true;
    }
    
    // false 表示有环
    // true 表示无环
    public boolean check2(Link header) {
        // 每次走一步
        Link check1 = header;
        // 每次走两步
        Link check2 = header;
        while (check1 != null && check2 != null) {
            //
            check1 = move(check1);
            check2 = move(move(check2));
            if (check1 == null || check2 == null) {
                break;
            }
            if (check1 == check2) {
                return false;
            }
        }
        return true;
    }
    
    public Link move(Link link) {
        if (link == null) {
            return null;
        } else {
            return link.child;
        }
    }
    
    public class Link {
        public String value;
        public Link child;
    }
}
