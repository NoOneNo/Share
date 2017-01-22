package com.hengye.share.module.sso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.ui.widget.X5WebView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.FileUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.rxjava.DefaultSubscriber;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.ThirdPartyUtils.WeiboApp;
import com.hengye.share.util.thirdparty.WBUtil;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by yuhy on 16/7/11.
 */
public class WeiboWebLoginActivity extends BaseActivity {

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, WeiboWebLoginActivity.class);

        intent.putExtra("account", UserUtil.getCurrentUser().getAccount());
        intent.putExtra("password", UserUtil.getCurrentUser().getPassword());
        return intent;
    }


    private boolean isLoginUrl(String url) {
        return url != null && url.toLowerCase().startsWith(WBUtil.URL_HTTPS_LOGIN);
    }

    private View mLoading;
    X5WebView mWebView;
    WeiboApp mApp;
    String mAccount, mPassword;
    String mUrl;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_weibo_web_login;
    }

    @Override
    protected void handleBundleExtra(Intent intent) {
        super.handleBundleExtra(intent);
        String appKey = intent.getStringExtra("appKey");
        if (appKey != null) {
            mApp = WeiboApp.valueOf(appKey);
        } else {
            mApp = WeiboApp.SHARE;
        }
        mAccount = intent.getStringExtra("account");
        mPassword = intent.getStringExtra("password");

        mUrl = WBUtil.URL_HTTPS_LOGIN;
//        mUrl = getIntent().getStringExtra("url");
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoading = findViewById(R.id.loading);
        mWebView = (X5WebView) findViewById(R.id.web_view);

        mWebView.setWebViewClient(new WeiboWebViewClient());
        mWebView.setWebChromeClient(new WeiboWebChromeClient());

        loadLoginUrl();
    }

    private class WeiboWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            L.debug("load url = %s", view.getUrl());
            //如果上一个页面是登录页面，并且跳转到http://m.weibo.cn则表示登录成功
            if (!CommonUtil.isEmpty(url) && isLoginUrl(mUrl) && url.toLowerCase().startsWith("http://m.weibo.cn")) {
                L.debug("登录成功");

                mWebView.loadUrl("javascript:getAccount()");

                String cookie = CookieManager.getInstance().getCookie(WBUtil.URL_HTTPS_MOBILE);
                String accountCookie = UserUtil.getCurrentUser().getCookie();
                if (CommonUtil.isEmpty(accountCookie) || (!CommonUtil.isEmpty(cookie) && !cookie.equalsIgnoreCase(accountCookie))) {
                    saveCookie(cookie);
                    L.debug("手动记录cookie");
                }
            }

            mUrl = url;
            view.loadUrl(url);
            L.debug("overriderUrlLoading = %s", url);

            return true;
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebViewClient.a a) {
            super.onReceivedError(webView, webResourceRequest, a);
            ToastUtil.showToast("出现错误");
        }
    }

    private class WeiboWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress < 100) {
                mLoading.setVisibility(View.VISIBLE);
            } else if (newProgress == 100) {
                mLoading.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }
        }

    }

    private void saveCookie(String cookie) {

        User user = UserUtil.getCurrentUser();
        user.setCookie(cookie);
        UserUtil.updateUser(user);
        // 获取到cookie后，保存到账号

        setResult(Activity.RESULT_OK);
        finish();
    }

    private void loadLoginUrl() {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> subscriber) {
                        String url;
                        try {
                            url = getEncapsulationLoginUrl();
                        } catch (UnknownHostException uhe) {
                            subscriber.onError(uhe);
                            return;
                        }
                        if (url == null) {
                            subscriber.onError(new Throwable("get encapsulation login url fail"));
                        } else {
                            subscriber.onNext(url);
                        }
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new DefaultSubscriber<String>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            ToastUtil.showNetWorkErrorToast();
                        } else {
                            ToastUtil.showToastError(R.string.tip_unpredictable_error);
                        }
                        mLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(String s) {

                        mWebView.loadDataWithBaseURL(WBUtil.URL_HTTPS_LOGIN, s, "text/html", "UTF-8", "");
                        mWebView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.loadUrl("javascript:fillAccount()");
                                mLoading.setVisibility(View.GONE);
                            }
                        }, 1500);
                    }
                });
    }

    private String getEncapsulationLoginUrl() throws UnknownHostException {
        int count = 3;
        while (count-- >= 0) {
            try {
                String js = FileUtil.readAssetsFile("mobile.js");
                if (CommonUtil.noEmpty(mAccount, mPassword)) {
//                    js = js.replace("%username%", mAccount).replace("%password%", mPassword);
                }

                Document dom = Jsoup.connect(WBUtil.URL_HTTPS_LOGIN).get();
                String html = dom.toString();
                html = html.replace("</head>", js + "</head>");
                return html;

            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof UnknownHostException) {
                    throw (UnknownHostException) e;
                }
            }
        }
        return null;
    }
}
