package com.hengye.share.ui.activity.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.dialog.DialogStyleHelper;
import com.hengye.share.ui.widget.dialog.ListDialog;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.helper.SettingHelper;
import com.hengye.share.util.ToastUtil;

import java.util.ArrayList;

public class WebViewActivity extends BaseActivity{

    @Override
    protected void handleBundleExtra(Intent intent) {
        mUrl = getIntent().getStringExtra("url");

        if (mUrl == null) {
            Uri data = intent.getData();
            if (data != null && data.toString() != null) {
                String value = data.toString();
                int start = value.indexOf("http://");
                if (start != -1) {
                    mUrl = value.substring(start + 7);
                }
            }
        }

//        if (mUrl != null) {
//            mUrl.startsWith()
//            if (!mUrl.startsWith("http") || !mUrl.startsWith("ftp")) {
//                mUrl = "http://" + mUrl;
//            }
//        }
    }

    public static Intent getStartIntent(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        return intent;
    }

    @Override
    protected boolean setToolBar() {
        return true;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (mUrl == null) {
            finish();
        } else {
            if(SettingHelper.isUseInternalBrowser()){
                initView();
            }else{
                openExternalBrowser(mUrl);
                finish();
            }
        }
    }

    private void openExternalBrowser(String url){
        final Uri uri = Uri.parse(url);
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if(IntentUtil.resolveActivity(intent)){
            startActivity(intent);
        }else{
            ToastUtil.showToast(R.string.label_resolve_url_activity_fail);
        }
    }

    private String mUrl;

    private WebView mWebView;
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

        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.web_view);
        mProgressBar = (ProgressBar) findViewById(R.id.loading);

        initToolbar();
        initListDialog();
        initWebView();

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
                        ClipboardUtil.copyAndToast(mWebView.getUrl());
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

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
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

//				DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//				Uri uri = Uri.parse(url);
//				DownloadManager.Request request = new DownloadManager.Request(uri);
//				//设置允许使用的网络类型，这里是移动网络和wifi都可以
////				request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE| DownloadManager.Request.NETWORK_WIFI);
//				request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//				//不显示下载界面
////				long id = downloadManager.enqueue(request);
//				downloadManager.enqueue(request);
//                ToastUtil.showToast("已经开始下载，详情查看通知栏");
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
    public void onBackPressed() {
//        if (mWebView.canGoBack()) {
//            mWebView.goBack();
//        } else {
            super.onBackPressed();
//        }
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
}
