package com.hengye.share.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.hengye.share.BaseApplication;
import com.hengye.share.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.lang.reflect.Type;

public class SPUtil {

    private SPUtil() {}

    private final static String APP_THEME = "theme";

    private final static String SINA_NAME = "sina";

    private final static String MODULE_NAME = "module";

    public static synchronized String getUid(){
        return getContext().getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).getString("uid", null);
    }

    public static synchronized void setUid(String uid){
        SharedPreferences.Editor editor = getContext().getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("uid", uid);
        editor.apply();
    }

//    public static synchronized String getSinaUid(){
//        Oauth2AccessToken oauth2AccessToken = getSinaAccessToken();
//        if(oauth2AccessToken == null){
//            return "";
//        }else{
//            return oauth2AccessToken.getUid();
//        }
//    }
//
//    public static synchronized String getSinaToken(){
//        Oauth2AccessToken oauth2AccessToken = getSinaAccessToken();
//        if(oauth2AccessToken == null){
//            return "";
//        }else{
//            return oauth2AccessToken.getToken();
//        }
//    }
//
//    public static synchronized Oauth2AccessToken getSinaAccessToken(){
//        String json = getContext().getSharedPreferences(SINA_NAME, Context.MODE_PRIVATE).getString("access_token", null);
//        if(!TextUtils.isEmpty(json)){
//            try{
//                return GsonUtil.getInstance().fromJson(json, Oauth2AccessToken.class);
//            }catch (JsonParseException e){
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    public static synchronized void setSinaAccessToken(Oauth2AccessToken accessToken){
//        setSinaAccessToken(GsonUtil.getInstance().toJson(accessToken));
//    }
//
//    public static synchronized void setSinaAccessToken(String json){
//        SharedPreferences.Editor editor = getContext().getSharedPreferences(SINA_NAME, Context.MODE_PRIVATE).edit();
//        editor.putString("access_token", json);
//        L.debug("save sina access_token , json : {}", json);
//        editor.apply();
//    }

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
