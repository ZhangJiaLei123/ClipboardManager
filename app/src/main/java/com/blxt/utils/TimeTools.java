package com.blxt.utils;

import java.util.Calendar;

/**
 * 时间工具,如时间比较等
 */
public class TimeTools {

    /**
     * 比较时间
     * @param calendar0
     * @param calendar
     * @return
     */
    public static String compareTime(Calendar calendar0, Calendar calendar){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int week = calendar0.get(Calendar.WEEK_OF_MONTH);
        int second = calendar.get(Calendar.SECOND);

        int year0 = calendar0.get(Calendar.YEAR);
        int month0 = calendar0.get(Calendar.MONTH);
        int day0 = calendar0.get(Calendar.DAY_OF_MONTH);
        int hour0 = calendar0.get(Calendar.HOUR_OF_DAY);
        int minute0 = calendar0.get(Calendar.MINUTE);
        int second0 = calendar0.get(Calendar.SECOND);
        int week0 = calendar0.get(Calendar.WEEK_OF_MONTH);

        if(calendar.getTimeInMillis() >= calendar0.getTimeInMillis()){ // 当前时间大于旧时间
            // 年
            if(calendar.getTimeInMillis() - calendar0.getTimeInMillis() >= 360 * 1 * 24 * 60 * 60 * 1000 && year != year0){
                return year - year0 + "很久之前";
            }
            // 月
            else if(calendar.getTimeInMillis() - calendar0.getTimeInMillis() >= 30 * 1 * 24 * 60 * 60 * 1000 && month != month0){
                return month - month0 + "个月之前";
            }
            // 周
            else if(calendar.getTimeInMillis() - calendar0.getTimeInMillis() >= 7 * 1 * 24 * 60 * 60 * 1000 && week != week0){
                return week - week0 + "周之前";
            }
            // 天
            else
            {
                int d = day - day0;
                if(d > 0){
                    return d + "天前";
                }
                else{
                    int h = hour - hour0;
                    if(h > 0){
                        return h + "小时前";
                    }else{
                        int m = minute - minute0;
                        if (m > 0){
                            return m + "分钟前";
                        }
                        else{
                            return "刚刚";
                        }
                    }
                }
            }
            // 时
            // 分

        }
        else{
            return "未来";
        }

    }



}
