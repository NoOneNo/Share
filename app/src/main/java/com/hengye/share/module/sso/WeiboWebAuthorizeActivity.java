package com.hengye.share.module.sso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hengye.share.R;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.FileUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.Utility;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;
import com.hengye.share.util.thirdparty.ThirdPartyUtils.WeiboApp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * Created by yuhy on 16/7/11.
 */
public class WeiboWebAuthorizeActivity extends BaseActivity {

    public static Intent getStartIntent(Context context, String appKey, String uid){
        Intent intent = new Intent(context, WeiboWebAuthorizeActivity.class);
        intent.putExtra("appKey", appKey);
        intent.putExtra("uid", uid);
        return intent;
    }

    public static final String URL_OAUTH2_ACCESS_AUTHORIZE = "https://api.weibo.com/oauth2/authorize";
    public static final String URL_BASE = "https://api.weibo.com";

    LoadingDialog mLoadingDialog;
    WebView mWebView;
    WeiboApp mApp;
    View mLoading;
    String mAccount, mPassword;
    boolean mHasAutoFillAccount = false;


    @Override
    public int getLayoutResId() {
        return R.layout.activity_weibo_web_authorize;
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

        String uid = getIntent().getStringExtra("uid");

        if(uid != null){
            User user = uid.equals(UserUtil.getUid()) ?
                    UserUtil.getCurrentUser() : UserUtil.getUser(uid);
            if(user != null){
                mAccount = user.getAccount();
                mPassword = user.getPassword();
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoadingDialog = new LoadingDialog(this, getString(R.string.tip_loading));

        mLoading = findViewById(R.id.loading);
        mWebView = (WebView) findViewById(R.id.web_view);

        mWebView.setWebViewClient(new WeiboWebViewClient());
        mWebView.setWebChromeClient(new WeiboWebChromeClient());
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        mWebView.addJavascriptInterface(new LoginJavaScriptInterface(), "loginjs");

        loadData();
//        mWebView.loadUrl(getOAuthUrl());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    private void loadData() {
        Single
                .create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(SingleEmitter<String> subscriber) {
                        String url = null;
                        try {
                            url = getEncapsulationLoginUrl();
                        } catch (UnknownHostException uhe) {
                            subscriber.onError(uhe);
                            return;
                        }
                        if (url == null) {
                            subscriber.onError(new Throwable("get encapsulation login url fail"));
                        } else {
                            subscriber.onSuccess(url);
                        }
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new SingleObserver<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {

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
                    public void onSuccess(String s) {
                        mWebView.loadDataWithBaseURL(URL_BASE, s, "text/html", "UTF-8", "");
                    }
                });
    }

    final class LoginJavaScriptInterface {

        public LoginJavaScriptInterface() {
        }

        @JavascriptInterface
        public void setAccount(String account, String password) {
            mAccount = account;
            mPassword = password;
        }

    }

    private class WeiboWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            L.debug("load url = %s", view.getUrl());
            // 授权成功
            if (url != null && url.startsWith(ThirdPartyUtils.getWeiboRedirectUrl(mApp))) {
                handleRedirectUrl(url);
                // 把WebView隐藏，因为重定向页面可能是个错误的页面
                mWebView.setVisibility(View.INVISIBLE);
                return false;
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            ToastUtil.showToast("出现错误");
            mLoading.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mLoading.setVisibility(View.GONE);
        }
    }

    private class WeiboWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress < 100) {
//                mProgressBar.setVisibility(View.VISIBLE);
//                mProgressBar.setProgress(newProgress);

                L.debug("progress = %d , url = %s", newProgress, view.getUrl());
            } else if (newProgress == 100) {
//                mProgressBar.setVisibility(View.GONE);

                L.debug("progress = 100 , url = %s", view.getUrl());

                // 填充账号
                if (CommonUtil.noEmpty(mAccount, mPassword, view.getUrl())) {
                    if (!mHasAutoFillAccount && view.getUrl().equalsIgnoreCase("about:blank")) {
                        L.debug("fillAccount(%s, %s)", mAccount, mPassword);
                        mWebView.loadUrl("javascript:fillAccount()");
                        mHasAutoFillAccount = true;
                    }
                }
                mLoading.setVisibility(View.GONE);
            }
        }
    }

    private void handleRedirectUrl(String url) {
        Bundle values = Utility.parseUrl(url);
        String error = values.getString("error");
        String error_code = values.getString("error_code");
        String code = values.getString("code");

        if (error == null && error_code == null && code != null) {
            L.debug("授权成功, code = " + code);

            mLoadingDialog.show();

//            client_id=2403130832&
//                    client_secret=2ef1ce4d1349aecbff2cbfa73637c38c&
//                    grant_type=authorization_code&
//                    code=d1a089df9eb52c695aab5fea4af6ac3d
//                    &redirect_uri=https://api.weibo.com/oauth2/default.html
            UrlBuilder ub = new UrlBuilder();
            ub.addParameter("client_id", ThirdPartyUtils.getAppKeyForWeibo(mApp));
            ub.addParameter("client_secret", ThirdPartyUtils.getAppSecretForWeibo(mApp));
            ub.addParameter("grant_type", "authorization_code");
            ub.addParameter("redirect_uri", ThirdPartyUtils.getWeiboRedirectUrl(mApp));
            ub.addParameter("code", code);
            RetrofitManager
                    .getWBService()
                    .oauthToken(ub.getParameters())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new SingleObserver<HashMap<String, String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(HashMap<String, String> map) {
                            mLoadingDialog.dismiss();
                            if (!CommonUtil.isEmpty(map)) {
                                Bundle bundle = new Bundle();
                                for (Map.Entry<String, String> entry : map.entrySet()) {
                                    bundle.putString(entry.getKey(), entry.getValue());
                                }
                                bundle.putString("account", mAccount);
                                bundle.putString("password", mPassword);
                                bundle.putString("appKey", mApp.name());
                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                setResult(RESULT_OK, intent);
                            }
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            mLoadingDialog.dismiss();
                            ToastUtil.showNetWorkErrorToast();
                            finish();
                        }
                    });
        } else {
            ToastUtil.showToast(R.string.tip_cancel_authorize);
            finish();
        }
    }

    private String getOAuthUrl() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", ThirdPartyUtils.getAppKeyForWeibo(mApp));
//        parameters.put("response_type", "token");
        parameters.put("redirect_uri", ThirdPartyUtils.getWeiboRedirectUrl(mApp));
        parameters.put("display", "mobile");
        if (mApp == WeiboApp.WEICO) {
            parameters.put("forcelogin", "true");
        }
        return URL_OAUTH2_ACCESS_AUTHORIZE + "?" + Utility.encodeUrl(parameters);
//                + "&scope=friendships_groups_read,friendships_groups_write";
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            ToastUtil.showToast(R.string.tip_cancel_authorize);
            finish();
        }
    }

    private String getEncapsulationLoginUrl() throws UnknownHostException {
        int count = 3;
        while (count-- >= 0) {
            try {
                String js = FileUtil.readAssetsFile("oauth.js");
                if (CommonUtil.noEmpty(mAccount, mPassword)) {
                    js = js.replace("%username%", mAccount).replace("%password%", mPassword);
                }

                Document dom = Jsoup.connect(getOAuthUrl()).get();
                String html = dom.toString();
                html = html.replace("<html>", "<html id='all' >").replace("</head>", js + "</head>")
                        .replace("action-type=\"submit\"", "action-type=\"submit\" id=\"submit\"");

                // 通过监听input标签的oninput事件，来获取账户密码
                // onchange是value改变，且焦点改变才触发
                // oninput是value改变就触发
                try {
                    dom = Jsoup.parse(html);
                    Element inputAccount = dom.select("input#userId").first();
                    inputAccount.attr("oninput", "getAccount()");

                    Element pwdAccount = dom.select("input#passwd").first();
                    pwdAccount.attr("oninput", "getAccount()");

                    L.debug(inputAccount.toString());
                    L.debug(pwdAccount.toString());

                    html = dom.toString();

                    L.debug("添加input监听事件");
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                L.debug("封装后的url : \n%s", html);

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
