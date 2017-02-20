package com.hengye.share.util;


import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil extends ApplicationUtil{

    private SPUtil() {}

    private final static String APP_SETTING_NAME = "app_setting";

    private final static String APP_THEME = "theme";

    private final static String SINA_NAME = "sina";

    private final static String MODULE_NAME = "module";

    public static SharedPreferences getSharedPreferences(){
        return getContext().getSharedPreferences(APP_SETTING_NAME, Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(String key, boolean defValue){
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public static String getString(String key, String defValue){
        return getSharedPreferences().getString(key, defValue);
    }

    public static int getInt(String key, int defValue){
        return getSharedPreferences().getInt(key, defValue);
    }

    public static void putBoolean(String key, boolean value){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putString(String key, String value){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putInt(String key, int value){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static synchronized String getUid(){
        return getSharedPreferences().getString("uid", null);
    }

    public static synchronized void setUid(String uid){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString("uid", uid);
        editor.apply();
    }

//    public static synchronized <T> T getModule(Type type, String name){
//        String json = getSharedPreferences().getString(name, null);
//        if(!TextUtils.isEmpty(json)){
//            try{
//                return GsonUtil.fromJson(json, type);
//            }catch (JsonParseException e){
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    public static synchronized <T> T getModule(Class<T> clazz, String name){
//        String json = getSharedPreferences().getString(name, null);
//        if(!TextUtils.isEmpty(json)){
//            try{
//                return GsonUtil.fromJson(json, clazz);
//            }catch (JsonSyntaxException e){
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//
//    public static synchronized <T> void setModule(T t, String name){
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        editor.putString(name, GsonUtil.toJson(t));
//        L.debug("save module, name : %s, json : %s", t.getClass().getSimpleName(), GsonUtil.toJson(t));
//        editor.apply();
//    }
}
