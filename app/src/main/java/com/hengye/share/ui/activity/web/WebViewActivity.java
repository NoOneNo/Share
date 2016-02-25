package com.hengye.share.ui.activity.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.util.IntentUtil;

public class WebViewActivity extends BaseActivity {

    @Override
    protected void handleBundleExtra() {
        mUrl = getIntent().getStringExtra("url");

        if (mUrl == null) {
            Uri data = getIntent().getData();
            if (data != null && data.toString() != null) {
                String value = data.toString();
                int start = value.indexOf("http://");
                if (start != -1) {
                    mUrl = value.substring(start + 7);
                }
            }
        }
    }

    public static Intent getStartIntent(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        return intent;
    }

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (mUrl == null) {
            finish();
        } else {
            initView();
        }
    }

    private String mUrl;

    private WebView mWebView;

    private LoadingDialog mLoadingDialog;

    private void initView() {

        initToolbar();

        mWebView = (WebView) findViewById(R.id.web_view);

        mLoadingDialog = new LoadingDialog(this);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mLoadingDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoadingDialog.dismiss();
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

        mWebView.loadUrl(mUrl);
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
