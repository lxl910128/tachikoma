package club.projectgaia.tachikoma.algorithm;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Phoenix Luo
 * @version 2021/3/2
 **/
public class TimeDifference {
    public static void main(String[] args) throws ParseException {
        
        TimeDifference test = new TimeDifference();
        String small = "1949-10-01";
        String big = "2020-04-06";
        assert (daysBetween(small, big) == test.interval(small, big));
        System.out.println("success");
    }
    
    /**
     * 计算日期差
     *
     * @param first
     * @param second
     * @return
     */
    public  int interval(String first, String second) {
        // 先判断 first 和 second 哪个谁大谁小
        // 保证second 大
        Integer f = Integer.valueOf(first.replace("-", ""));
        Integer s = Integer.valueOf(second.replace("-", ""));
        if (s < f) {
            String tmp = first;
            first = second;
            second = tmp;
        }
        
        // 分别计算 年月日差值并相加
        String[] firstStr = first.split("-");
        String[] secondStr = second.split("-");
        Integer firstYear = Integer.valueOf(firstStr[0]);
        Integer firstMonth = Integer.valueOf(firstStr[1]);
        Integer firstDay = Integer.valueOf(firstStr[2]);
        
        Integer secYear = Integer.valueOf(secondStr[0]);
        Integer secMonth = Integer.valueOf(secondStr[1]);
        Integer sectDay = Integer.valueOf(secondStr[2]);
        
        int[] ret = {0, 0};
        // 计算日期到当年1月1日的天数
        int firstDayOfYear = getDayOfYear(firstYear, firstMonth, firstDay);
        int secDayOfYear = getDayOfYear(secYear, secMonth, sectDay);
        // 计算dayOfYear的差
        if (firstDayOfYear <= secDayOfYear) {
            ret[0] = secDayOfYear - firstDayOfYear;
        } else {
            //借年位
            ret[0] = (checkYear(secYear - 1) ? 366 : 365) + secDayOfYear - firstDayOfYear;
            secYear--;
        }
        //计算年的差值
        for (; firstYear < secYear; firstYear++) {
            ret[1] += checkYear(firstYear) ? 366 : 365;
        }
        return ret[0] + ret[1];
    }
    
    // 获取日期到当年1月1日的天数
    public int getDayOfYear(int year, int month, int day) {
        int ret = 0;
        for (int i = 1; i < month; i++) {
            ret += getMonthOfDay(i, year);
        }
        ret += day;
        return ret;
    }
    
    public int getMonthOfDay(Integer month, Integer year) {
        int ret = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                ret = 31;
                break;
            case 02:
                if (checkYear(Integer.valueOf(year))) {
                    ret = 29;
                } else {
                    ret = 28;
                }
                break;
            default:
                ret = 30;
        }
        return ret;
    }
    
    // 判断闰年
    public boolean checkYear(Integer year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
    
    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long time1 = sdf.parse(smdate).getTime();
        long time2 = sdf.parse(bdate).getTime();
        long between_days;
        if (time2 > time1) {
            between_days = (time2 - time1) / (1000 * 3600 * 24);
        } else {
            between_days = (time1 - time2) / (1000 * 3600 * 24);
        }
        return Integer.parseInt(String.valueOf(between_days));
    }
}
