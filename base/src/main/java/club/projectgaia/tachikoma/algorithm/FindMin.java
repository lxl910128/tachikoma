package club.projectgaia.tachikoma.algorithm;

import javafx.util.Pair;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * @author Phoenix Luo
 * @version 2021/3/1
 **/
public class FindMin {
    public static void main(String[] args) {
        // 给定1个二维矩阵，以及N各坐标点
        // 计算矩阵中每个点，到给定点的距离，距离是曼哈顿距离
        //   2  1  2
        //   1  0  1    3 * 3 给定点是 1，1
        //   2  1  2
        
    }
    
    // inMap 0是x长度 1是y长度；point 是坐标数组
    // 输出二维数组到标记点的
    public int[][] find(int[] inMap, int[][] points) {
        int maxX = inMap[0];
        int maxY = inMap[1];
        int[][] ret = new int[maxX][maxY];
        // x;y min
        Queue<Pair<String, Integer>> queue = new ArrayDeque<>();
        // 记录变量过得
        Set<String> visited = new HashSet<>();
        
        // 基础放入队列
        for (int[] p : points) {
            ret[p[0]][p[1]] = 0;
            String pStr = p[0] + ";" + p[1];
            queue.add(new Pair<>(pStr, 0));
            visited.add(pStr);
        }
        while (!queue.isEmpty()) {
            // 处理点
            Pair<String, Integer> p = queue.poll();
            // xy
            String[] xyStr = p.getKey().split(";");
            int[] xy = {Integer.valueOf(xyStr[0]), Integer.valueOf(xyStr[1])};
            
            int addX = xy[0] + 1;
            int addY = xy[1] + 1;
            int incX = xy[0] - 1;
            int incY = xy[1] - 1;
            //找合规 上下左右 加入队列
            if (addX < maxX && !visited.contains(addX + ";" + xy[1])) {
                queue.add(new Pair<>(addX + ";" + xy[1], p.getValue() + 1));
            }
            if (incX >= 0 && !visited.contains(incX + ";" + xy[1])) {
                queue.add(new Pair<>(incX + ";" + xy[1], p.getValue() + 1));
            }
            if (addY < maxY && !visited.contains(xy[0] + ";" + addY)) {
                queue.add(new Pair<>(xy[0] + ";" + addY, p.getValue() + 1));
            }
            if (incY >= 0 && !visited.contains(xy[0] + ";" + incY)) {
                queue.add(new Pair<>(xy[0] + ";" + incY, p.getValue() + 1));
            }
        }
        return ret;
    }
}
