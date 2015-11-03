package com.hengye.share.util;


import android.content.Context;
import android.content.SharedPreferences;

public class SPUtil {
    private static SPUtil ourInstance = new SPUtil();

    public static SPUtil getInstance() {
        return ourInstance;
    }

    private Context mContext;
//    private SharedPreferences mSP;
//    private SharedPreferences.Editor editor;
//    private final static String DEFAULT_NAME = "share";

    private SPUtil() {
    }

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

    public String getSinaAccessToken(){
        return mContext.getSharedPreferences(SINA_NAME, Context.MODE_PRIVATE).getString("access_token", null);
    }

    public void setAccessToken(String value){
        SharedPreferences.Editor editor = mContext.getSharedPreferences(SINA_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("access_token", value);
        editor.apply();
    }

}
