package com.hengye.share.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class IntentUtil extends ApplicationUtil{

    private IntentUtil(){

    }

    /**
     * 检测系统是否可以打开相应的intent;
     * @param intent
     * @return
     */
    public static boolean resolveActivity(Intent intent){
        return intent != null && intent.resolveActivity(getContext().getPackageManager()) != null;
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

}
