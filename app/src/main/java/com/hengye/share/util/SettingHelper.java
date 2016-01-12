package com.hengye.share.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.hengye.share.BaseApplication;
import com.hengye.share.R;

import java.util.Set;

public class SettingHelper {

    //基本设置
    public final static String KEY_BASIC_SWIPE_BACK = "swipe_back";
    //基本设置

    //流量控制
    public final static String KEY_FLOW_PREREAD = "preread";
    public final static String KEY_FLOW_LOAD_COUNT = "load_count";
    public final static String KEY_FLOW_PHOTO_DOWNLOAD_QUALITY = "photo_download_quality";
    public final static String KEY_FLOW_PHOTO_UPLOAD_QUALITY = "photo_upload_quality";
    //流量控制

    //音效管理
    public final static String KEY_SOUND_OFF = "sound_off";
    public final static String KEY_SOUND_REFRESH_RINGTONE = "refresh_ringtone";
    public final static String KEY_SOUND_VIBRATION_ON = "vibration_on";
    public final static String KEY_SOUND_VIBRATION_FEEDBACK = "vibration_feedback";
    //音效管理

    //通知设置
    public final static String KEY_NOTIFY_OPEN = "notify_open";
    public final static String KEY_NOTIFY_NO_REMIND_AT_NIGHT = "no_remind_at_night";
    public final static String KEY_NOTIFY_FREQUENCY = "notify_frequency";
    public final static String KEY_NOTIFY_TYPE = "remind_type";
    public final static String KEY_NOTIFY_RINGTONE = "remind_by_ringtone";
    public final static String KEY_NOTIFY_VIBRATION = "remind_by_vibration";
    public final static String KEY_NOTIFY_LIGHTS = "remind_by_lights";
    //通知设置

    //主题
    public final static String KEY_THEME_APP = "theme_app";
    //主题

    //阅读习惯
    public final static String KEY_HABIT_FONT_ZOOM = "font_zoom";
    public final static String KEY_HABIT_MENU_STYLE = "menu_show_style";
    public final static String KEY_HABIT_SHOW_AVATOR = "show_avator";
    public final static String KEY_HABIT_AUTO_NIGHT = "auto_night_mode";
    public final static String KEY_HABIT_READ_ORDER = "read_order";
    //阅读习惯

    public static SharedPreferences getPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
    }

    public final static String THEME_COLOR_BLUE= "blue";
    public final static String THEME_COLOR_GREEN = "green";
    public final static String THEME_COLOR_DEFAULT = THEME_COLOR_GREEN;
    public final static int THEME_RES_ID_DEFAULT = R.style.ShareAppTheme_Green;

    //主题颜色
    public static String getAppThemeColor(){
        return getPreferences().getString(KEY_THEME_APP, null);
    }

    public static void setAppThemeColor(String color){
        getPreferences().edit().putString(KEY_THEME_APP, color).apply();
    }

    public static int getAppThemeResId(){
        return getAppThemeResId(getAppThemeColor());
    }

    public static int getAppThemeResId(String color){
        if(TextUtils.isEmpty(color)){
            return THEME_RES_ID_DEFAULT;
        }else if(color.equals(THEME_COLOR_BLUE)){
            return R.style.ShareAppTheme_Blue;
        }else if(color.equals(THEME_COLOR_GREEN)){
            return R.style.ShareAppTheme_Green;
        }else{
            return THEME_RES_ID_DEFAULT;
        }
    }

    //滑动退出
    public static String getSwipeBack(){
        return getPreferences().getString(KEY_BASIC_SWIPE_BACK, null);
    }

    //滑动退出
    public static boolean isSwipeBack(){
        String value = getPreferences().getString(KEY_BASIC_SWIPE_BACK, null);
        if("4".equals(value)){
            return false;
        }
        return true;
    }

    //预读模式
    public static boolean isPreRead(){
       return getPreferences().getBoolean(KEY_FLOW_PREREAD, true);
    }

    //加载内容条数
    public static String getLoadCount(){
        return getPreferences().getString(KEY_FLOW_LOAD_COUNT, null);
    }

    //图片加载质量
    public static String getPhotoDownloadQuality(){
        return getPreferences().getString(KEY_FLOW_PHOTO_DOWNLOAD_QUALITY, null);
    }

    //图片上传质量
    public static String getPhotoUploadQuality(){
        return getPreferences().getString(KEY_FLOW_PHOTO_UPLOAD_QUALITY, null);
    }

    //静音模式
    public static boolean isSoundOff(){
        return getPreferences().getBoolean(KEY_SOUND_OFF, false);
    }

    //刷新音效
    public static boolean isRefreshRingTone(){
        if(!isSoundOff()){
            return getPreferences().getBoolean(KEY_SOUND_REFRESH_RINGTONE, true);
        }else{
            return false;
        }
    }

    //开启震动
    public static boolean isVibrationOn(){
        return getPreferences().getBoolean(KEY_SOUND_VIBRATION_ON, true);
    }

    //震动反馈
    public static boolean isVibrationFeedBack(){
        if(isVibrationOn()){
            return getPreferences().getBoolean(KEY_SOUND_OFF, true);
        }else{
            return false;
        }

    }

    //开启通知
    public static boolean isNotifyOpen(){
        return getPreferences().getBoolean(KEY_NOTIFY_OPEN, true);
    }

    //夜间免打扰
    public static boolean isNotifyNoRemindAtNightOpen(){
        return getPreferences().getBoolean(KEY_NOTIFY_NO_REMIND_AT_NIGHT, true);
    }

    //提醒时间间隔
    public static String getNotifyFrequency(){
        return getPreferences().getString(KEY_NOTIFY_FREQUENCY, null);
    }

    //提醒类型
    public static Set<String> getNotifyType(){
        return getPreferences().getStringSet(KEY_NOTIFY_TYPE, null);
    }

    //铃声提醒
    public static String getRingtone(){
        return getPreferences().getString(KEY_NOTIFY_RINGTONE, null);
    }

    public static void setRingtone(String path){
        getPreferences().edit().putString(KEY_NOTIFY_RINGTONE, path).apply();
    }

    //通知震动提醒
    public static boolean isNotifyVibrationOn(){
        return getPreferences().getBoolean(KEY_NOTIFY_VIBRATION, true);
    }

    //呼吸灯提醒
    public static boolean isNotifyLightsOn(){
        return getPreferences().getBoolean(KEY_NOTIFY_LIGHTS, true);
    }

    //字体大小
    public static String getFontZoom(){
        return getPreferences().getString(KEY_HABIT_FONT_ZOOM, null);
    }

    //功能菜单风格
    public static String getMenuStyle(){
        return getPreferences().getString(KEY_HABIT_MENU_STYLE, null);
    }

    //显示头像
    public static boolean isShowAvator(){
        return getPreferences().getBoolean(KEY_HABIT_SHOW_AVATOR, true);
    }

    //自动进入夜间模式
    public static boolean isAutoNightMode(){
        return getPreferences().getBoolean(KEY_HABIT_AUTO_NIGHT, true);
    }

    //阅读顺序
    public static String getReadingOrder(){
        return getPreferences().getString(KEY_HABIT_READ_ORDER, null);
    }
}
