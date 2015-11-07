package com.hengye.share.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.lang.reflect.Type;

public class SPUtil {
    private static SPUtil ourInstance = new SPUtil();

    public static SPUtil getInstance() {
        return ourInstance;
    }

    private Context mContext;

    private SPUtil() {}

    public void init(Context context){
        mContext = context;
    }

    /**
     * sina sharedpreference 文件头
     *
     * 键值对对应如下:
     * access_token : value
     *
     */
    private final static String SINA_NAME = "sina";

    private final static String MODULE_NAME = "module";

    public synchronized Oauth2AccessToken getSinaAccessToken(){
        String json = mContext.getSharedPreferences(SINA_NAME, Context.MODE_PRIVATE).getString("access_token", null);
        if(!TextUtils.isEmpty(json)){
            try{
                return GsonUtil.getInstance().fromJson(json, Oauth2AccessToken.class);
            }catch (JsonParseException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public synchronized void setSinaAccessToken(Oauth2AccessToken accessToken){
        setSinaAccessToken(GsonUtil.getInstance().toJson(accessToken));
    }

    public synchronized void setSinaAccessToken(String json){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(SINA_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("access_token", json);
        L.debug("save sina access_token , json : {}", json);
        editor.apply();
    }

    public synchronized <T> T getModule(Type type, String name){
        String json = mContext.getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).getString(name, null);
        if(!TextUtils.isEmpty(json)){
            try{
                return GsonUtil.getInstance().fromJson(json, type);
            }catch (JsonParseException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public synchronized <T> T getModule(Class<T> clazz, String name){
        String json = mContext.getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).getString(name, null);
        if(!TextUtils.isEmpty(json)){
            try{
                return GsonUtil.getInstance().fromJson(json, clazz);
            }catch (JsonSyntaxException e){
                e.printStackTrace();
            }
        }
        return null;
    }


    public synchronized <T> void setModule(T t, String name){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(name, GsonUtil.getInstance().toJson(t));
        L.debug("save module, name : {}, json : {}", t.getClass().getSimpleName(), GsonUtil.getInstance().toJson(t));
        editor.apply();
    }

}
