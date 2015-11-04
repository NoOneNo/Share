package com.hengye.share.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

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

    public Oauth2AccessToken getSinaAccessToken(){
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

    public void setSinaAccessToken(Oauth2AccessToken accessToken){
        setSinaAccessToken(GsonUtil.getInstance().toJson(accessToken));
    }
    private void setSinaAccessToken(String json){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(SINA_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("access_token", json);
        L.debug("save sina access_token , json : {}", json);
        editor.apply();
    }

}
