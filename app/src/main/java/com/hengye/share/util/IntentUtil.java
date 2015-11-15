package com.hengye.share.util;

import android.content.Context;
import android.content.Intent;

public class IntentUtil {

    private IntentUtil(){

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
