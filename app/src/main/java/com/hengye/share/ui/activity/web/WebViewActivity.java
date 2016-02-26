package com.hengye.share.ui.activity.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.widget.dialog.DialogStyleHelper;
import com.hengye.share.ui.widget.dialog.ListDialog;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.ViewUtil;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends BaseActivity implements View.OnClickListener {

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

        if (mUrl != null) {
            if (!mUrl.startsWith("http") || !mUrl.startsWith("ftp")) {
                mUrl = "http://" + mUrl;
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
    private ListDialog mListDialog;

    private ArrayList<ListDialog.KeyValue> getListDialogData() {
        ArrayList<ListDialog.KeyValue> data = new ArrayList<>();
        data.add(new ListDialog.KeyValue(R.drawable.compose_camerabutton_background_highlighted, "刷新"));
        data.add(new ListDialog.KeyValue(R.drawable.compose_camerabutton_background_highlighted, "复制链接"));
        data.add(new ListDialog.KeyValue(R.drawable.compose_camerabutton_background_highlighted, "在浏览器中打开"));
        return data;
    }

    private void initListDialog() {
        mListDialog = new ListDialog(this, getListDialogData());
        DialogStyleHelper.setWebViewListDialogStyle(mListDialog);

        mListDialog.setOnItemListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        mWebView.reload();
                        break;
                    case 1:
                        ClipboardUtil.copy(mWebView.getUrl());
                        ToastUtil.showToast(R.string.label_copy_url_to_clipboard_success);
                        break;
                    case 2:
                        Uri uri = Uri.parse(mWebView.getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (IntentUtil.resolveActivity(intent)) {
                            startActivity(intent);
                        }else{
                            ToastUtil.showToast(R.string.label_resolve_url_activity_fail);
                        }
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

    private void initView() {

        mLoadingDialog = new LoadingDialog(this);
        mWebView = (WebView) findViewById(R.id.web_view);
        findViewById(R.id.btn_menu).setOnClickListener(this);

        initToolbar();
        initListDialog();
        initWebView();


        mWebView.loadUrl(mUrl);
    }

    private void initWebView(){
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
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

//        mWebView.se
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_menu) {
            if (mListDialog.isShowing()) {
                mListDialog.dismiss();
            } else {
                mListDialog.show();
            }
        }
    }
}
