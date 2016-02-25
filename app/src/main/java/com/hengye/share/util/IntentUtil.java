package com.hengye.share.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hengye.share.ui.base.BaseApplication;

public class IntentUtil {

    private IntentUtil(){

    }

    /**
     * 检测系统是否可以打开相应的intent;
     * @param intent
     * @return
     */
    public static boolean resolveActivity(Intent intent){
        if(intent != null && intent.resolveActivity(BaseApplication.getInstance().getPackageManager()) != null){
            return true;
        }
        return false;
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
