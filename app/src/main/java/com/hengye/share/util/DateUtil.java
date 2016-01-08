package com.hengye.share.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {


    public static String getChinaGMTDateFormat() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+800")); // 设置时区为GMT
        return sdf.format(cd.getTime());
    }

    public static Date getChinaGMTDate() {
        Calendar cd = Calendar.getInstance();
        return cd.getTime();
    }

    //    "Fri Nov 06 21:39:48 +0800 2015" 格林时间格式
    //    EEE MMM dd hh:mm:ss z yyyy
    public static Date getGMTDate(String time) {
        Date date = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.US);
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getLatestDateFormat(Date date) {
        if (date == null) {
            return "未知";
        }

        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarEver = Calendar.getInstance();
        calendarEver.setTime(date);

        long duration = calendarNow.getTimeInMillis() - calendarEver.getTimeInMillis();

        long day = duration / (24 * 60 * 60 * 1000);
        long hour = (duration / (60 * 60 * 1000) - day * 24);
        long minute = ((duration / (60 * 1000)) - day * 24 * 60 - hour * 60);
//        long second = (duration / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        int nowHour = calendarNow.get(Calendar.HOUR_OF_DAY);

        if (day == 0 && nowHour >= hour) {
            //今天
            if (hour >= 1) {
                return "今天 " + new SimpleDateFormat("HH:mm", Locale.US).format(date);
            } else if (minute >= 1) {
                return minute + "分钟前";
            } else {
                return "刚刚";
            }
        } else {
            //昨天
            if ((day == 0 && nowHour < hour) || day == 1 && nowHour >= hour) {
                return "昨天 " + new SimpleDateFormat("HH:mm", Locale.US).format(date);
            } else {
                //大于2天 显示日期
                String pattern;
                //如果是今年就不显示年份
                if (calendarNow.get(Calendar.YEAR) == calendarEver.get(Calendar.YEAR)) {
                    pattern = "M-d";
                } else {
                    pattern = "yyyy-M-d";
                }

                SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
                return formatter.format(date);
            }
        }
    }

    public static String getLatestDateFormat(String time) {
        if(TextUtils.isEmpty(time)){
            return "未知";
        }
        return getLatestDateFormat(getGMTDate(time));
    }
}


































