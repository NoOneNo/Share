//package com.hengye.share.ui.activity.web;
//
//import io.influx.library.influxappinfo.AppInfo;
//import io.influx.library.influxbase.BaseActivity;
//import io.influx.library.influxbase.BaseImagePicker;
//import io.influx.library.influxbase.BaseInfo;
//import io.influx.library.influxextension.ExtensionUtils;
//import io.influx.library.influxinitial.R;
//import io.influx.library.influxshare.InfluxShareParams;
//import io.influx.library.influxshare.InfluxShareView;
//import io.influx.library.influxshare.WeiXinApiManager;
//import io.influx.library.swaphandle.SwapDeclare;
//import io.influx.library.swaphandle.SwapDeclareBean;
//import io.influx.library.swaphandle.SwapHandle;
//import io.influx.library.swaphandle.SwapParamBean;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import android.app.Activity;
//import android.app.DownloadManager;
//import android.app.DownloadManager.Request;
//import android.content.ClipData;
//import android.content.ClipboardManager;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.webkit.DownloadListener;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageButton;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class ShowWebContentActivity extends BaseActivity implements SwapDeclare{
//
//	public static final int FILECHOOSER_RESULTCODE = 100;
//
//	private WebView webView;
//	private ImageButton backButton;
//	private ImageButton shareButton;
//	private TextView titlebarTitle;
//	private View titlebarview;
//	private WebParams params;
//	private ProgressBar titlebarpgbar;
//	private String thisUrl;
//	private String influxCopy;
//	private String influxShare;
//	private String influxShareEnable;
//
//	private ValueCallback<Uri> mUploadMessage;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.library_show_web_content);
//
//		webView = (WebView) findViewById(R.id.library_show_web_content_web_view);
//		titlebarview = findViewById(R.id.library_show_web_content_titlebar01);
//		backButton = (ImageButton) titlebarview.findViewById(R.id.library_show_web_content_titlebar01_back);
//		titlebarTitle = (TextView) titlebarview.findViewById(R.id.library_show_web_content_titlebar01_title);
//		titlebarpgbar = (ProgressBar) titlebarview.findViewById(R.id.library_show_web_content_titlebar01_pgbar);
//		shareButton = (ImageButton) titlebarview.findViewById(R.id.library_show_web_content_titlebar01_share);
//
//		webView.getSettings().setJavaScriptEnabled(true);
//		backButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				ShowWebContentActivity.this.finish();
//			}});
//
//		webView.setWebViewClient(new WebViewClient() {
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				//重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
////				Log.i("felix", "influxWebViewUrl="+url);
//				if (url.startsWith("weixin://")) {
//					//跳转微信
//					WeiXinApiManager.getManager().openWXApp();
//				}
//				if(!url.startsWith("http://")){
//					if(url.toLowerCase().startsWith(AppInfo.getInstance(getApplicationContext()).getUrlScheme().toLowerCase())){
//						//自定义内容
//						Map<String,Object> result = SwapHandle.uranusinnerHandle(url);
//						if(result != null && result.size() == 4){
//							String className = (String) result.get(SwapHandle.CLASS_NAME);
//							Map<String,Object> paramsMap = (Map<String, Object>) result.get(SwapHandle.MAP_PARAMS);
//							Class targetClass = (Class) result.get(SwapHandle.TAGET_CLASS);
//							SwapParamBean[] paramsArray = (SwapParamBean[]) result.get(SwapHandle.TAGET_PARAMS_ARRAY);
//
//							String mainClassName = BaseInfo.getMainActivityName(getApplicationContext());
//							if(className.equals(mainClassName)){
//								Map<String,Object> params = (Map<String, Object>) result.get(SwapHandle.MAP_PARAMS);
//								BaseInfo.updateMainActivityParams(getApplicationContext(), params);
//								for(Activity ac : BaseActivity.activityList){
//									ac.finish();
//								}
//							}else{
//								if(paramsArray != null){
//									SwapHandle.startActivity(ShowWebContentActivity.this, targetClass, paramsArray);
//								}else {
//									SwapHandle.startActivity(ShowWebContentActivity.this, targetClass);
//								}
//							}
//						}else {
//							view.loadUrl(url);
//						}
//
//					}else{
//							//外部调用
//							Intent intent = new Intent(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//							if(ExtensionUtils.isIntentAvailable(intent)){
//								startActivity(intent);
//								finish();
//						}
//					}
//				}else {
//					view.loadUrl(url);
//				}
//				return true;
//            }
//
//			@Override
//			public void onPageStarted(WebView view, String url, Bitmap favicon) {
//				super.onPageStarted(view, url, favicon);
//				titlebarpgbar.setVisibility(View.VISIBLE);
//			}
//
//
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				super.onPageFinished(view, url);
//				if(params != null && params.getFinishedJavascript() != null && !params.getFinishedJavascript().equals("")){
//					webView.loadUrl("javascript:" + params.getFinishedJavascript());
//				}
//				thisUrl = url;
//				influxCopy = "";
//				influxShare = "";
//				influxShareEnable = "";
//				if (url.contains("#")) {
//					String[] urlPath = url.split("#");
//					for (int i = 0; i < urlPath.length; i++) {//获取分享参数
//						if (urlPath[i].startsWith("influx_copy="))influxCopy = urlPath[i].substring("influx_copy=".length());
//						if (urlPath[i].startsWith("influx_share_enable="))influxShareEnable = urlPath[i].substring("influx_share_enable=".length());
//						if (urlPath[i].startsWith("influx_share="))influxShare = urlPath[i].substring("influx_share=".length());
//					}
//				}
//
//				if (influxCopy != null && !"".equals(influxCopy)) {//判断是否复制内容到剪切板
//					copyText(influxCopy);
//					Toast.makeText(ShowWebContentActivity.this, "成功复制到剪切板!", Toast.LENGTH_LONG).show();
//				}else if (influxShare != null && !"".equals(influxShare)) {//判断是否分享
//					initInfluxShare(influxShare);
//				}
//
//				titlebarpgbar.setVisibility(View.GONE);
//				if (influxShareEnable != null && !"".equals(influxShareEnable)) {//判断是否显示分享按钮
//					if (influxShareEnable.equals("1")) {
//						shareButton.setVisibility(View.VISIBLE);
//					} else {
//						shareButton.setVisibility(View.GONE);
//					}
//				}
//			}
//		});
//		webView.setWebChromeClient(new WebChromeClient(){
//
//			public void openFileChooser(ValueCallback<Uri> uploadMsg,
//	                String acceptType, String capture) {
//	            mUploadMessage = uploadMsg;
//	            SwapHandle.startActivityForResult(ShowWebContentActivity.this, BaseImagePicker.class, FILECHOOSER_RESULTCODE);
//	        }
//
//	        // 3.0 + 调用这个方法
//	        public void openFileChooser(ValueCallback<Uri> uploadMsg,
//	                String acceptType) {
//	            mUploadMessage = uploadMsg;
//	            SwapHandle.startActivityForResult(ShowWebContentActivity.this, BaseImagePicker.class, FILECHOOSER_RESULTCODE);
//	        }
//
//	        // Android < 3.0 调用这个方法
//	        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//	            mUploadMessage = uploadMsg;
//	            SwapHandle.startActivityForResult(ShowWebContentActivity.this, BaseImagePicker.class, FILECHOOSER_RESULTCODE);
//	        }
//		});
//		webView.setDownloadListener(new DownloadListener() {
//			@Override
//			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
//                    long contentLength) {
////				Uri uri = Uri.parse(url);
////	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
////	            startActivity(intent);
////	            finish();
//
//				DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//				Uri uri = Uri.parse(url);
//				Request request = new Request(uri);
//				//设置允许使用的网络类型，这里是移动网络和wifi都可以
//				request.setAllowedNetworkTypes(Request.NETWORK_MOBILE| Request.NETWORK_WIFI);
//				request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//				//不显示下载界面
//				request.setVisibleInDownloadsUi(false);
////				long id = downloadManager.enqueue(request);
//				downloadManager.enqueue(request);
//				Toast.makeText(getApplicationContext(), "已经开始下载，详情查看通知栏", Toast.LENGTH_LONG).show();
//			}
//		});
//
//		if(getIntent().getExtras() != null && getIntent().getExtras().getSerializable(SwapHandle.PARAMS_TAG) != null) {//封装参数调用
//			params = (WebParams) getIntent().getExtras().getSerializable(SwapHandle.PARAMS_TAG);
//			titlebarTitle.setText(params.getTitle());
//			webView.loadUrl(params.getUrl());
//		}else {
//			Toast.makeText(getApplicationContext(), "亲，没有链接地址哦", Toast.LENGTH_LONG).show();
//		}
//
//		shareButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {//右上角分享按钮
//				initInfluxShare(thisUrl);
//			}
//		});
//	}
//
//	/**
//	 * 复制到剪切板
//	 * @param text
//	 */
//	private void copyText(String text){
//		ClipboardManager cManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//		cManager.setPrimaryClip(ClipData.newPlainText("influx_copy", text));
//	}
//
//	/** 初始化 分享*/
//	private void initInfluxShare(String shareUrl) {
//
//		//设置分享内容
//		InfluxShareParams params = new InfluxShareParams();
//		params.setShareTitle("");
//		params.setShareText(shareUrl);
//		params.setShareUrl(shareUrl);
//
//		//设置可分享方式
//		List<String> shareKeylist = new ArrayList<String>();
//		shareKeylist.add(InfluxShareParams.share_mode_wechat);
//		shareKeylist.add(InfluxShareParams.share_mode_wechatmoment);
//		shareKeylist.add(InfluxShareParams.share_mode_qq);
//		shareKeylist.add(InfluxShareParams.share_mode_qzone);
////		shareKeylist.add(InfluxShareParams.share_mode_sinaweibo);//未通过审核
//		shareKeylist.add(InfluxShareParams.share_mode_message);
//		shareKeylist.add(InfluxShareParams.share_mode_email);
//
//		SwapParamBean adb = new SwapParamBean(InfluxShareView.SHARE_SDK_KEY, AppInfo.getInstance(ShowWebContentActivity.this).getProperties().get("share_sdk_key").toString());
//		SwapParamBean adb2 = new SwapParamBean(InfluxShareView.SHARE_MODE_LIST,shareKeylist);
//		SwapParamBean adb3 = new SwapParamBean(InfluxShareView.SHARE_PARAMS,params);
//		SwapHandle.startActivity(ShowWebContentActivity.this, InfluxShareView.class , adb,adb2,adb3);
//
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    if (requestCode == FILECHOOSER_RESULTCODE
//	    		&& resultCode == RESULT_OK
//	    		&& mUploadMessage != null
//	    		&& data != null) {
//	        if (null == mUploadMessage) return;
//	        String filePath = data.getStringExtra(BaseImagePicker.FILE_PATH);
//	        if(filePath != null){
//	        	Uri uri = Uri.fromFile(new File(filePath));
//	        	mUploadMessage.onReceiveValue(uri);
//	        }
//	    }else {
//	    	mUploadMessage.onReceiveValue(null);
//	    }
//	}
//
//	//-------------------------------------------------------------------
//	@Override
//	public boolean needPreLogin() {
//		return false;
//	}
//
//	@Override
//	public List<SwapDeclareBean> getDeclares() {
//		List<SwapDeclareBean> list = new ArrayList<SwapDeclareBean>();
//		SwapDeclareBean sdb = new SwapDeclareBean(SwapHandle.PARAMS_TAG, WebParams.class, false);
//		list.add(sdb);
//		return list;
//	}
//	//-------------------------------------------------------------------
//}