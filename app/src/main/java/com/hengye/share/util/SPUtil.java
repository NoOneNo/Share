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
//    private static SPUtil ourInstance = new SPUtil();
//
//    public static SPUtil getInstance() {
//        return ourInstance;
//    }

//    private Context mContext;

    private SPUtil() {}

//    public void init(Context context){
//        mContext = context;
//    }

    private final static String APP_THEME = "theme";

    private final static String SINA_NAME = "sina";

    private final static String MODULE_NAME = "module";


    public static String getAppTheme(){
        return BaseApplication.getInstance().getSharedPreferences(APP_THEME, Context.MODE_PRIVATE).getString("color", THEME_COLOR_DEFAULT);
    }

    public static void setAppTheme(String color){
        SharedPreferences.Editor editor = BaseApplication.getInstance().getSharedPreferences(APP_THEME, Context.MODE_PRIVATE).edit();
        editor.putString("color", color);
        L.debug("save theme, color : {}", color);
        editor.apply();
    }

    public final static String THEME_COLOR_BLUE= "blue";
    public final static String THEME_COLOR_GREEN = "green";
    public final static String THEME_COLOR_DEFAULT = THEME_COLOR_BLUE;
    public final static int THEME_RES_ID_DEFAULT = R.style.ShareAppTheme_Blue;

    public static int getAppThemeResId(){
        return getAppThemeResId(getAppTheme());
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



    public static synchronized Oauth2AccessToken getSinaAccessToken(){
        String json = BaseApplication.getInstance().getSharedPreferences(SINA_NAME, Context.MODE_PRIVATE).getString("access_token", null);
        if(!TextUtils.isEmpty(json)){
            try{
                return GsonUtil.getInstance().fromJson(json, Oauth2AccessToken.class);
            }catch (JsonParseException e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static synchronized void setSinaAccessToken(Oauth2AccessToken accessToken){
        setSinaAccessToken(GsonUtil.getInstance().toJson(accessToken));
    }

    public static synchronized void setSinaAccessToken(String json){
        SharedPreferences.Editor editor = BaseApplication.getInstance().getSharedPreferences(SINA_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("access_token", json);
        L.debug("save sina access_token , json : {}", json);
        editor.apply();
    }

    public static synchronized <T> T getModule(Type type, String name){
        String json = BaseApplication.getInstance().getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).getString(name, null);
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
        String json = BaseApplication.getInstance().getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).getString(name, null);
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
        SharedPreferences.Editor editor = BaseApplication.getInstance().getSharedPreferences(MODULE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(name, GsonUtil.getInstance().toJson(t));
        L.debug("save module, name : {}, json : {}", t.getClass().getSimpleName(), GsonUtil.getInstance().toJson(t));
        editor.apply();
    }

}
