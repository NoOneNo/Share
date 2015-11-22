package com.hengye.share.util;

import android.content.Context;
import android.content.Intent;

public class IntentUtil {

    private IntentUtil(){

    }

    public static void startActivity(Context context, Class clazz){
        startActivity(context, new Intent(context, clazz));
    }

    public static void startActivity(Context context, Intent intent){
        context.startActivity(intent);
    }

    public static void startActivityIfTokenValid(Context context, Intent intent){
        if(false){
            //如果不合法
            //启动登录activity;
        }else{
            context.startActivity(intent);
        }
    }
}