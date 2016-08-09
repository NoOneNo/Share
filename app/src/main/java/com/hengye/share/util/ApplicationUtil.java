package com.hengye.share.util;

import android.app.Application;
import android.content.res.Resources;

import com.hengye.share.ui.base.BaseApplication;

public class ApplicationUtil {
    public static Application getContext(){
        return BaseApplication.getInstance();
    }

    public static Resources getResources(){
        return getContext().getResources();
    }
}
