package com.hengye.share.util;

import android.app.Application;

import com.hengye.share.ui.base.BaseApplication;

public class ApplicationUtil {
    protected static Application getContext(){
        return BaseApplication.getInstance();
    }
}
