package com.hengye.share.module.setting;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.hengye.share.module.base.BaseApplication;
import com.hengye.share.R;

import java.util.HashMap;
import java.util.Set;

public class SettingHelper {

    public static HashMap<String, Object> mCache = new HashMap<>();

    //基本设置
    public final static String KEY_BASIC_SWIPE_BACK = "swipe_back";
    public final static String KEY_BASIC_HOME_BACK = "home_back";
    public final static String KEY_BASIC_CLICK_TO_CLOSE_GALLERY = "click_photo_back";
    public final static String KEY_BASIC_CLEAR_PHOTO_CACHE = "clear_photo_cache";
    public final static String KEY_BASIC_INTERNAL_BROWSER = "internal_browser";
    public final static String KEY_BASIC_PHOTO_SAVE_PATH = "photo_save_path";
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
    public final static String KEY_HABIT_SHOW_AVATAR = "show_avatar";
    public final static String KEY_HABIT_AUTO_NIGHT = "auto_night_mode";
    public final static String KEY_HABIT_READ_ORDER = "read_order";
    //阅读习惯

    //关于
    public final static String KEY_ABOUT_CHECK_UPDATE = "check_update";
    public final static String KEY_ABOUT_VERSION_CODE = "version_code";
    public final static String KEY_ABOUT_DONATE_TO_DEVELOPER = "donate_to_developer";
    //关于

    public static SharedPreferences getPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
    }

    public final static String THEME_COLOR_NIGHT = "night";
    public final static String THEME_COLOR_BLUE= "blue";
    public final static String THEME_COLOR_GREEN = "green";
    public final static String THEME_COLOR_PINK = "pink";
    public final static String THEME_COLOR_DEFAULT = THEME_COLOR_GREEN;
    public final static int THEME_RES_ID_DEFAULT = R.style.ShareAppTheme_Green;

    //主题颜色
    public static String getAppThemeColor(){
        return getString(KEY_THEME_APP);
    }

    public static void setAppThemeColor(String color){
        putString(KEY_THEME_APP, color);
    }

    public static int getAppThemeResId(){
        return getAppThemeResId(getAppThemeColor());
    }

    public static int getAppThemeResId(String color){
        if(TextUtils.isEmpty(color)){
            return THEME_RES_ID_DEFAULT;
        }else if(color.equals(THEME_COLOR_NIGHT)){
            return R.style.ShareAppTheme_DayNight;
        }else if(color.equals(THEME_COLOR_BLUE)){
            return R.style.ShareAppTheme_Blue;
        }else if(color.equals(THEME_COLOR_GREEN)){
            return R.style.ShareAppTheme_Green;
        }else if(color.equals(THEME_COLOR_PINK)){
            return R.style.ShareAppTheme_Pink;
        }else{
            return THEME_RES_ID_DEFAULT;
        }
    }

    //首页按返回键退出
    public static boolean isExitOnBackPressed(){
        return getBoolean(KEY_BASIC_HOME_BACK, false);
    }

    //滑动退出
    public static String getSwipeBack(){
        return getString(KEY_BASIC_SWIPE_BACK);
    }

    //滑动退出
    public static boolean isSwipeBack(){
        String value = getSwipeBack();
        return !"4".equals(value);
    }

    //是否单击图片返回上一层
    public static boolean isClickToCloseGallery(){
        return getBoolean(KEY_BASIC_CLICK_TO_CLOSE_GALLERY, true);
    }

    //是否使用内置的浏览器
    public static boolean isUseInternalBrowser(){
        return getBoolean(KEY_BASIC_INTERNAL_BROWSER, true);
    }

    //预读模式
    public static boolean isPreRead(){
        return getBoolean(KEY_FLOW_PREREAD, true);
    }

    //加载内容条数
    public static int getLoadCount(){

        String value = getString(KEY_FLOW_LOAD_COUNT, "1");
        if("1".equals(value)){
            return 30;
        }else if("2".equals(value)){
            return 50;
        }else if("3".equals(value)){
            return 70;
        }else if("4".equals(value)){
            return 50;
        }else{
            return 30;
        }
    }

    //图片加载质量
    public static String getPhotoDownloadQuality(){
        return getString(KEY_FLOW_PHOTO_DOWNLOAD_QUALITY);
    }

    //图片上传质量
    public static String getPhotoUploadQuality(){
        return getString(KEY_FLOW_PHOTO_UPLOAD_QUALITY);
    }

    //静音模式
    public static boolean isSoundOff(){
        return getBoolean(KEY_SOUND_OFF, false);
    }

    //刷新音效
    public static boolean isRefreshRingTone(){
        if(!isSoundOff()){
            return getBoolean(KEY_SOUND_REFRESH_RINGTONE, true);
        }else{
            return false;
        }
    }

    //开启震动
    public static boolean isVibrationOn(){
        return getBoolean(KEY_SOUND_VIBRATION_ON, true);
    }

    //震动反馈
    public static boolean isVibrationFeedBack(){
        if(isVibrationOn()){
            return getBoolean(KEY_SOUND_OFF, true);
        }else{
            return false;
        }
    }

    //开启通知
    public static boolean isNotifyOpen(){
        return getBoolean(KEY_NOTIFY_OPEN, true);
    }

    //夜间免打扰
    public static boolean isNotifyNoRemindAtNightOpen(){
        return getBoolean(KEY_NOTIFY_NO_REMIND_AT_NIGHT, true);
    }

    //提醒时间间隔
    public static String getNotifyFrequency(){
        return getString(KEY_NOTIFY_FREQUENCY);
    }

    //提醒类型
    public static Set<String> getNotifyType(){
        return getStringSet(KEY_NOTIFY_TYPE);
    }

    //铃声提醒
    public static String getRingtone(){
        return getString(KEY_NOTIFY_RINGTONE);
    }

    public static void setRingtone(String path){
        putString(KEY_NOTIFY_RINGTONE, path);
    }

    //通知震动提醒
    public static boolean isNotifyVibrationOn(){
        return getBoolean(KEY_NOTIFY_VIBRATION, true);
    }

    //呼吸灯提醒
    public static boolean isNotifyLightsOn(){
        return getBoolean(KEY_NOTIFY_LIGHTS, true);
    }

    //字体大小
    public static String getFontZoom(){
        return getString(KEY_HABIT_FONT_ZOOM);
    }

    //功能菜单风格
    public static String getMenuStyle(){
        return getString(KEY_HABIT_MENU_STYLE);
    }

    //显示头像
    public static Set<String> isShowAvatar(){
        return getStringSet(KEY_HABIT_SHOW_AVATAR);
    }

    public static boolean isShowTopicAvatar(){
        Set<String> value = isShowAvatar();
        return value == null || !value.contains("1");
    }

    public static boolean isShowCommentAndRepostAvatar(){
        Set<String> value = isShowAvatar();
        return value == null || !value.contains("2");
    }

    //自动进入夜间模式
    public static boolean isAutoNightMode(){
        return getBoolean(KEY_HABIT_AUTO_NIGHT, true);
    }

    //阅读顺序
    public static String getReadingOrder(){
        return getString(KEY_HABIT_READ_ORDER);
    }

    public static boolean isOrderReading(){
        return true;
        // TODO: 2016/10/20  
        //逆序阅读实现有问题，先不做
//        String value = getString(KEY_HABIT_READ_ORDER, "1");
//        if("1".equals(value)){
//            return true;
//        }
//        return false;
    }


    public static String getString(String key){
        return getString(key, null);
    }

    public static String getString(String key, String defaultValue){
        String value = (String)mCache.get(key);

        if(value == null){
            value = getPreferences().getString(key, defaultValue);
            putValue(key, value);
        }
        return value;
    }

    public static Boolean getBoolean(String key, Boolean defaultValue){
        Boolean value = (Boolean)mCache.get(key);

        if(value == null){
            value = getPreferences().getBoolean(key, defaultValue);
            putValue(key, value);
        }
        return value;
    }

    public static Set<String> getStringSet(String key){
        return getStringSet(key, null);
    }

    public static Set<String> getStringSet(String key, Set<String> defaultValue){
        Set<String> value = (Set<String>)mCache.get(key);

        if(value == null){
            value = getPreferences().getStringSet(key, defaultValue);
            putValue(key, value);
        }
        return value;
    }

    public static void putValue(String key, Object value){
        mCache.put(key, value);
    }

    public static void putString(String key, String value){
        putValue(key, value);
        getPreferences().edit().putString(key, value).apply();
    }

    public static void putBoolean(String key, Boolean value){
        putValue(key, value);
        getPreferences().edit().putBoolean(key, value).apply();
    }

    public static void removeCache(String key){
        mCache.remove(key);
    }

    public static void resetCache(){
        mCache.clear();
    }
}
