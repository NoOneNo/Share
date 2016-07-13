package com.hengye.share.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hengye.share.R;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.mvpview.MvpView;
import com.hengye.share.ui.presenter.BasePresenter;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.Utility;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;
import com.sina.weibo.sdk.auth.WeiboParameters;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yuhy on 16/7/11.
 */
public class WeiboWebLoginActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_weibo_web_login;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);

        mLoadingDialog = new LoadingDialog(this, getString(R.string.tip_loading));

        mWebView = (WebView) findViewById(R.id.web_view);

        mWebView.setWebViewClient(new WeiboWebViewClient());

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        mWebView.loadUrl(getWeiboOAuthUrl());
    }

    LoadingDialog mLoadingDialog;
    WebView mWebView;

    public static final String DIRECT_URL = "https://api.weibo.com/oauth2/default.html";

//    public static final String URL_OAUTH2_ACCESS_AUTHORIZE = "https://open.weibo.cn/oauth2/authorize";
    public static final String URL_OAUTH2_ACCESS_AUTHORIZE = "https://api.weibo.com/oauth2/authorize";

    private String getWeiboOAuthUrl() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", ThirdPartyUtils.getAppKeyForWeibo());
//        parameters.put("response_type", "token");
        parameters.put("redirect_uri", DIRECT_URL);
        parameters.put("display", "mobile");
        return URL_OAUTH2_ACCESS_AUTHORIZE + "?" + Utility.encodeUrl(parameters);
//                + "&scope=friendships_groups_read,friendships_groups_write";
    }

    private class WeiboWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (url.startsWith(DIRECT_URL)) {
                handleRedirectUrl(view, url);
                view.stopLoading();
                return;
            }
            super.onPageStarted(view, url, favicon);
        }

        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            ToastUtil.showToast("出现错误");
//            new SinaWeiboErrorDialog().show(getSupportFragmentManager(), "");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            if (!url.equals("about:blank")) {
//            }
        }
    }

    private void handleRedirectUrl(WebView view, String url) {
        Bundle values = Utility.parseUrl(url);
        String error = values.getString("error");
        String error_code = values.getString("error_code");
        String code = values.getString("code");

        if (error == null && error_code == null && code != null) {
            mLoadingDialog.show();

//            client_id=2403130832&
//                    client_secret=2ef1ce4d1349aecbff2cbfa73637c38c&
//                    grant_type=authorization_code&
//                    code=d1a089df9eb52c695aab5fea4af6ac3d
//                    &redirect_uri=https://api.weibo.com/oauth2/default.html
            UrlBuilder ub = new UrlBuilder();
            ub.addParameter("client_id", ThirdPartyUtils.getAppKeyForWeibo());
            ub.addParameter("client_secret", ThirdPartyUtils.getAppSecretForWeibo());
            ub.addParameter("grant_type", "authorization_code");
            ub.addParameter("redirect_uri", DIRECT_URL);
            ub.addParameter("code", code);
            RetrofitManager
                    .getWBService()
                    .oauthToken(ub.getParameters())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<HashMap<String, String>>(){
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            mLoadingDialog.dismiss();
                            ToastUtil.showNetWorkErrorToast();
                            finish();
                        }

                        @Override
                        public void onNext(HashMap<String, String> map) {
                            mLoadingDialog.dismiss();
                            if(!CommonUtil.isEmpty(map)){
                                Bundle bundle = new Bundle();
                                for(Map.Entry<String, String> entry : map.entrySet()){
                                    bundle.putString(entry.getKey(), entry.getValue());
                                }
                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                setResult(RESULT_OK, intent);
                            }
                            finish();
                        }
                    });

        } else {
            ToastUtil.showToast(R.string.label_cancel_authorize);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            ToastUtil.showToast(R.string.label_cancel_authorize);
            finish();
        }
    }
}
