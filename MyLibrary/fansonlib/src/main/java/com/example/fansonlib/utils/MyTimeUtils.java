package com.example.fansonlib.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by：fanson
 * Created on：2016/12/20 16:35
 * Describe：
 */
public class MyTimeUtils {

    /**
     * 比较两个日期之间的大小
     *
     * @param d1
     * @param d2
     * @return 前者大于后者返回true 反之false
     */
    public static boolean compareDate(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        int result = c1.compareTo(c2);
        if (result >= 0)
            return true;
        else
            return false;
    }



}
