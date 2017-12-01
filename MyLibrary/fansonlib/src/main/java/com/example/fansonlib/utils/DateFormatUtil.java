package com.example.fansonlib.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by：fanson
 * Created on：2016/9/29 15:59
 * Describe：将String类型的时间转化为指定格式
 */
public class DateFormatUtil {

    /**
     * 获取"yyyy/MM/dd HH:mm"格式的时间
     * @param dateStr
     * @return
     */
    public static String format(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return formatter.format(parse(dateStr));
    }

    public static Date parse(String input ) throws java.text.ParseException {
        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSz" );
        //this is zero time so we need to add that TZ indicator for
        if ( input.endsWith( "Z" ) ) {
            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;
            String s0 = input.substring( 0, input.length() - inset );
            String s1 = input.substring( input.length() - inset, input.length() );
            input = s0 + "GMT" + s1;
        }
        return df.parse( input );
    }


    /**
     * 获取当前日期yyyy-MM-dd
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date());
    }

    /**
     * 获取当前日期字符串
     * @return
     */
    public static String getCurrentDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(new Date());
    }


    /**
     * 将字符串时间转为文字描述
     * @param time 输入格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getTimeDifference(String time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff ;
        try {
            Date publish_date = df.parse(time);
            Date now_date = new Date();
            diff = now_date.getTime() - publish_date.getTime();
            if (diff < 0) {
                return "from未来";
            } else {
                diff /= (1000 * 60);
                if (diff <= 60) {
                    return diff + "分钟前";
                }
                diff /= 60;
                if (diff <= 24) {
                    return diff + "小时前";
                }
                diff /= 24;
                if (diff < 30) {
                    if (diff == 1) {
                        return "昨天";
                    }
                }
//                DateFormat df_date = new SimpleDateFormat("MM-dd");
//                return df_date.format(publish_date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    /**
     * 获取当前年
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.MONTH)+1;
    }

    /**
     * 获取当前日
     * @return
     */
    public static int getCurrentDay() {
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    public static int getDayOfWeek(){
        Calendar cal=Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }
}
