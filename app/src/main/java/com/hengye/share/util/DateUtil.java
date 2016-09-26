package com.hengye.share.util;

import android.text.TextUtils;

import com.hengye.share.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    
    public static final String TIME_UNKOWN = ResUtil.getString(R.string.time_unknown);

    public static String formatDate(long time, String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return new SimpleDateFormat(format).format(cal.getTime());
    }

    public static String formatDate(String longStr, String format) {
        try {
            return formatDate(Long.parseLong(longStr), format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long formatStr(String timeStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(timeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String getChinaGMTDateFormat() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
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
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getEarlyDateFormat(String time) {
        if(TextUtils.isEmpty(time)){
            return TIME_UNKOWN;
        }
        return getEarlyDateFormat(getGMTDate(time));
    }

    public static String getEarlyDateFormat(Long time) {
        return getEarlyDateFormat(time, true);
    }

    public static String getEarlyDateFormat(Long time, boolean ignoreDiffer) {
        if(time == null || time == 0){
            return TIME_UNKOWN;
        }
        return getEarlyDateFormat(new Date(time), ignoreDiffer);
    }

    public static String getEarlyDateFormat(Date date) {
        return getEarlyDateFormat(date, true);
    }

    public static String getLaterDateFormat(String time) {
        if(TextUtils.isEmpty(time)){
            return TIME_UNKOWN;
        }
        return getLaterDateFormat(getGMTDate(time));
    }

    public static String getLaterDateFormat(Long time){
        return getLaterDateFormat(time, true);
    }

    public static String getLaterDateFormat(Long time, boolean ignoreDiffer){
        if(time == null || time == 0){
            return TIME_UNKOWN;
        }
        return getLaterDateFormat(new Date(time), ignoreDiffer);
    }

    public static String getLaterDateFormat(Date date){
        return getLaterDateFormat(date, true);
    }
    
    /**
     * 用一个以前的时间和现在的时间比较, 得到相差的大小后显示一个比较合适的时间格式;
     * @param date
     * @param ignoreDiffer 是否忽视相差的大小
     * @return
     */
    public static String getEarlyDateFormat(Date date, boolean ignoreDiffer) {
        if (date == null) {
            return TIME_UNKOWN;
        }

        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarEver = Calendar.getInstance();
        calendarEver.setTime(date);

        long duration = calendarNow.getTimeInMillis() - calendarEver.getTimeInMillis();

        if(!ignoreDiffer && duration < 0){
            //以前的时间比现在的时间大;
            return TIME_UNKOWN;
        }

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
                    pattern = ResUtil.getString(R.string.date_format_md);
                } else {
                    pattern = ResUtil.getString(R.string.date_format_ymd);
                }

                SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
                return formatter.format(date);
            }
        }
    }

    /**
     * 用一个未来的时间和现在的时间比较, 得到相差的大小后显示一个比较合适的时间格式;
     * @param date
     * @param ignoreDiffer 是否忽视相差的大小
     * @return
     */
    public static String getLaterDateFormat(Date date, boolean ignoreDiffer){
        if (date == null) {
            return TIME_UNKOWN;
        }

        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarEver = Calendar.getInstance();
        calendarEver.setTime(date);

        long duration = calendarEver.getTimeInMillis() - calendarNow.getTimeInMillis();

        if(!ignoreDiffer && duration < 0){
            //未来的时间比现在的时间小;
            return TIME_UNKOWN;
        }
        
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
                return minute + "分钟后";
            } else {
                return "1分钟内";
            }
        } else {
            //明天
            if ((day == 0 && nowHour < hour) || day == 1 && nowHour >= hour) {
                return "明天 " + new SimpleDateFormat("HH:mm", Locale.US).format(date);
            } else {
                //大于2天 显示日期
                String pattern;
                //如果是今年就不显示年份
                if (calendarNow.get(Calendar.YEAR) == calendarEver.get(Calendar.YEAR)) {
                    pattern = ResUtil.getString(R.string.date_format_md);
                } else {
                    pattern = ResUtil.getString(R.string.date_format_ymd);
                }

                SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
                return formatter.format(date);
            }
        }
    }
}


































