package com.hengye.share.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

public class X5WebView extends WebView {

    public X5WebView(Context context) {
        this(context, null, 0);
    }

    public X5WebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public X5WebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initWebViewSettings();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {
        setHapticFeedbackEnabled(true);

        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);//设置可以访问文件
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDomStorageEnabled(true);//支持dom存储

        settings.setSupportZoom(true);//支持缩放
//        settings.setBuiltInZoomControls(false);//设置是否显示缩放按钮
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//布局
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);//缩放至屏幕的大小
//        settings.setSupportMultipleWindows(true);//多窗口

        settings.setCacheMode(WebSettings.LOAD_NORMAL);

        settings.setAppCacheEnabled(true);
        settings.setGeolocationEnabled(true);//定位
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(getContext().getDir("web_databases", 0).getPath());

        settings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        getSettingsExtension为空
//        getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
    }
}