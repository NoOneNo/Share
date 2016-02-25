//package com.hengye.share.ui.activity.web;
//
//import org.apache.http.util.EncodingUtils;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.webkit.DownloadListener;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageButton;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//public class ShowWebActivity extends BaseActivity {
//
//	private WebView webView;
//
//	private TextView titleBarTitleTextView;
//	private ImageButton titleBarComeBack;
//	//private WebParams params;
//	private ProgressBar titleBarProgressBar;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_show_web_content);
//
//		initTitleBar();
//
//		webView = (WebView) findViewById(R.id.library_show_web_content_web_view);
//
//		webView.getSettings().setJavaScriptEnabled(true);
//
//		webView.setWebViewClient(new WebViewClient() {
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//					view.loadUrl(url);
//				return true;
//            }
//
//			@Override
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				super.onPageStarted(view, url, favicon);
//				titleBarProgressBar.setVisibility(View.VISIBLE);
//			}
//
//
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				super.onPageFinished(view, url);
////				if(params != null && params.getFinishedJavascript() != null && !params.getFinishedJavascript().equals("")){
////					webView.loadUrl("javascript:" + params.getFinishedJavascript());
////				}
//				titleBarProgressBar.setVisibility(View.GONE);
//			}
//		});
//		webView.setDownloadListener(new DownloadListener() {
//			@Override
//			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
//                    long contentLength) {
//				Uri uri = Uri.parse(url);
//	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//	            startActivity(intent);
//	            finish();
//			}
//		});
//
//		Intent intent = getIntent();
//		String url = intent.getStringExtra("url");
//		String title = intent.getStringExtra("title");
//		String method = intent.getStringExtra("method");
//		titleBarTitleTextView.setText(title);
//		if(method != null && method.equals("post"))
//		{
//			String params = intent.getStringExtra("params");
//			webView.postUrl(url, EncodingUtils.getBytes(params, "base64"));
//		}
//		else
//		{
//			webView.loadUrl(url);
//		}
//	}
//	private void initTitleBar()
//	{
//		titleBarTitleTextView = (TextView) findViewById(R.id.titlebar_title_tv);
//		titleBarTitleTextView.setText("图书查阅");
//
//		titleBarComeBack = (ImageButton) findViewById(R.id.titlebar_left_bt);
//		titleBarComeBack.setVisibility(View.VISIBLE);
//		titleBarComeBack.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if(webView.canGoBack())
//				{
//					webView.goBack();
//				}
//				else
//				{
//					ShowWebActivity.this.finish();
//				}
//			}
//		});
//
//		titleBarProgressBar = (ProgressBar) findViewById(R.id.titlebar_right_pgbar);
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//			webView.goBack();// 返回前一个页面
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//}
//
//
//
//
//
//
//
//
//
