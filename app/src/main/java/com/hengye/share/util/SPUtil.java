package com.hengye.share.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.hengye.share.ui.base.BaseApplication;

import java.lang.reflect.Type;

public class SPUtil {

    private SPUtil() {}

    private final static String APP_SETTING_NAME = "app_setting";

    private final static String APP_THEME = "theme";

    private final static String SINA_NAME = "sina";

    private final static String MODULE_NAME = "module";

    public static synchronized String getUid(){
        return getContext().getSharedPreferences(APP_SETTING_NAME, Context.MODE_PRIVATE).getString("uid", null);
    }

    public static synchronized void setUid(String uid){
        SharedPreferences.Editor editor = getContext().getSharedPreferences(APP_SETTING_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("uid", uid);
        editor.apply();
    }

    public static synchronized <T> T getModule(Type type, String name){
        String json = getContext().getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).getString(name, null);
        if(!TextUtils.isEmpty(json)){
            try{
                return GsonUtil.getInstance().fromJson(json, type);
            }catch (JsonParseException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static synchronized <T> T getModule(Class<T> clazz, String name){
        String json = getContext().getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).getString(name, null);
        if(!TextUtils.isEmpty(json)){
            try{
                return GsonUtil.getInstance().fromJson(json, clazz);
            }catch (JsonSyntaxException e){
                e.printStackTrace();
            }
        }
        return null;
    }


    public static synchronized <T> void setModule(T t, String name){
        SharedPreferences.Editor editor = getContext().getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(name, GsonUtil.getInstance().toJson(t));
        L.debug("save module, name : {}, json : {}", t.getClass().getSimpleName(), GsonUtil.getInstance().toJson(t));
        editor.apply();
    }

    private static BaseApplication getContext(){
        return BaseApplication.getInstance();
    }
}
