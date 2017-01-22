package com.hengye.share.module.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.sso.WeiboWebLoginActivity;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.X5WebView;
import com.hengye.share.ui.widget.dialog.DialogStyleHelper;
import com.hengye.share.ui.widget.dialog.ListDialog;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;

public class X5WebViewActivity extends BaseActivity {

    @Override
    protected void handleBundleExtra(Intent intent) {
        mUrl = getIntent().getStringExtra("url");

        if (mUrl == null) {
            Uri data = intent.getData();
            mUrl = data.getSchemeSpecificPart();
        }
    }


    public static Intent getStartIntent(Context context, String url) {
        Intent intent = new Intent(context, X5WebViewActivity.class);
        intent.putExtra("url", url);
        return intent;
    }

    @Override
    protected boolean setToolBar() {
        return true;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_webview_x5;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mUrl == null) {
            finish();
        } else {
            if (SettingHelper.isUseInternalBrowser()) {
                initView();
            } else {
                openExternalBrowser(mUrl);
                finish();
            }
        }
    }

    private void openExternalBrowser(String url) {
        final Uri uri = Uri.parse(url);
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (IntentUtil.resolveActivity(intent)) {
            startActivity(intent);
        } else {
            ToastUtil.showToastError(R.string.label_resolve_url_activity_fail);
        }
    }

    private String mUrl;
//    private boolean mIsWBUrl;
    private X5WebView mWebView;
    private CookieManager mCookieManager;
    private ProgressBar mProgressBar;
    private ListDialog mListDialog;

    private ArrayList<ListDialog.KeyValue> getListDialogData() {
        ArrayList<ListDialog.KeyValue> data = new ArrayList<>();
        data.add(new ListDialog.KeyValue(R.drawable.ic_refresh_white_48dp, "刷新"));
        data.add(new ListDialog.KeyValue(R.drawable.ic_content_copy_white_48dp, "复制链接"));
        data.add(new ListDialog.KeyValue(R.drawable.ic_open_in_browser_white_48dp, "在浏览器中打开"));
        return data;
    }

    private void initView() {

        mWebView = (X5WebView) findViewById(R.id.web_view);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);
        mCookieManager = CookieManager.getInstance();
        initToolbar();
        initListDialog();
        initWebView();

//        mIsWBUrl = WBUtil.isRequestWBResourceUrl(mUrl);
//        if(mIsWBUrl) {
//            updateWBCookie(mUrl);
//        }
        mWebView.loadUrl(mUrl);
    }

    private void initListDialog() {
        mListDialog = new ListDialog(this, getListDialogData());
        DialogStyleHelper.setWebViewListDialogStyle(mListDialog);

        mListDialog.setOnItemListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        mWebView.reload();
                        break;
                    case 1:
                        ClipboardUtil.copy(mWebView.getUrl());
                        ToastUtil.showToastSuccess(R.string.label_copy_url_to_clipboard_success);
                        break;
                    case 2:
                        openExternalBrowser(mWebView.getUrl());
                        break;
                    default:
                        break;
                }

                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListDialog.dismiss();
                    }
                }, 300);

            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.debug("shouldOverrideUrlLoading url : %s", url);
                if (url != null) {
                    Uri uri = Uri.parse(url);
                    if (!DataUtil.isHttpUrl(url)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        ComponentName componentName = intent.resolveActivity(view.getContext().getPackageManager());
                        if (componentName != null) {
                            L.debug("find url which is not http : %s", url);
                            view.getContext().startActivity(intent);
                            return true;
                        } else {
                            L.debug("find url which is not http , no app can open it: %s", url);
                        }
                    }else{
//                        if(mIsWBUrl){
//                            //发生重定向
//                            L.debug("微博url发生重定向跳转: {}, 当做cookie失效处理", url);
//                            UserUtil.clearCookie();
//                        }
                    }
                }

                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);

//                if(mIsWBUrl){
//                    updateWBCookie(url);
//                }
                L.debug("find cookies url : %s, values is %s", url, mCookieManager.getCookie(url));
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                if (IntentUtil.resolveActivity(intent)) {
                    startActivity(intent);
                }

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    public void onNavigationClick(View v) {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_more) {
            if (mListDialog.isShowing()) {
                mListDialog.dismiss();
            } else {
                mListDialog.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if (mWebView != null && !CommonUtil.isEmpty(mUrl)) {
                    mWebView.loadUrl(mUrl);
                }
            }
        }
    }

    private boolean mIsFirstTime = true;
    private int mUpdateWBCookieCount = 0;

    private void updateWBCookie(String url) {

        if(++mUpdateWBCookieCount > 3){
            return;
        }

        mCookieManager.removeAllCookie();

        String cookie = UserUtil.getCurrentUser().getCookie();

        if (CommonUtil.isEmpty(cookie)) {
            //请求登录
            if (mIsFirstTime) {
                mIsFirstTime = false;
                startActivityForResult(new Intent(X5WebViewActivity.this, WeiboWebLoginActivity.class), 2);
            }
        } else {
//            String currentCookie = mCookieManager.getCookie(url);
//
//            if (!cookie.equals(currentCookie)) {
//
//                //先清除之前的cookie
//                if (!CommonUtil.isEmpty(currentCookie)) {
//                    String[] currentCookies = currentCookie.split(";");
//                    for (String c : currentCookies) {
//                        if(c != null ){
//                            int index = c.indexOf("=");
//                            if(index != -1) {
//                                c = c.substring(0, index + 1);
//                            }
//                        }
//                        L.debug("clear cookie : {}", c);
//                        mCookieManager.setCookie(url, c);
//                    }
//                }
//            }

            String[] cookies = cookie.split(";");
            for(String c : cookies){
                L.debug("apply cookie : %s", c);
                mCookieManager.setCookie(url, c);
            }

            mCookieManager.flush();
        }
    }
}
