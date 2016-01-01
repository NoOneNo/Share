package com.hengye.share.util;

import android.app.Activity;
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

    public static void startActivityForResult(Activity activity, Class clazz, int requestCode){
        startActivityForResult(activity, new Intent(activity, clazz), requestCode);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode){
        activity.startActivityForResult(intent, requestCode);
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
