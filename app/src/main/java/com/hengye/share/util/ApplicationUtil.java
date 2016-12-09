package com.hengye.share.util;

import android.app.Application;

import com.hengye.share.module.base.BaseApplication;

public class ApplicationUtil {
    public static Application getContext(){
        return BaseApplication.getInstance();
    }
}
