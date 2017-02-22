package com.hengye.share.module.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.hengye.share.BuildConfig;
import com.hengye.share.service.VideoPlayService;
import com.hengye.share.util.AppUtils;
import com.hengye.share.util.L;
import com.hengye.share.util.NetworkUtil;
import com.hengye.share.util.RequestManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

public class BaseApplication extends Application{

	private static BaseApplication ourInstance;

	public final static int MAX_NETWORK_CACHE_SIZE = 200 * 1024 * 1024;

	public static BaseApplication getInstance(){
		return ourInstance;
	}

	@Override
	public void onCreate(){
		super.onCreate();
//		Debug.startMethodTracing("share");

		long start = System.currentTimeMillis();
		init();
		long end = System.currentTimeMillis();

		L.debug("application onCreate consume : %s", end - start);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
//		MultiDex.install(this);
	}

	private void init() {
//		startService(new Intent(this, ShareService.class));
		ourInstance = this;
		//初始化日志
		L.init();
		//监听activity生命周期
		registerActivityLifecycleCallbacks();
		//监听网络变化
		IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(new NetworkUtil.ObserveNetworkReceiver(), intentFilter);
//		SkinManager.setup(this);
		//如果程序是从被销毁恢复，栈中GuidanceActivity已不存在，所以不是所有初始化都可以放到GuidanceActivity
		//放到GuidanceActivity初始化
		//初始化volley
		RequestManager.init(this, null, MAX_NETWORK_CACHE_SIZE);
		if(BuildConfig.DEBUG){
			//监控内存泄漏
			refWatcher = LeakCanary.install(this);
		}else {
			//初始化腾讯bugly
			CrashReport.initCrashReport(BaseApplication.getInstance(), "900019432", false);
		}

		//初始化腾讯x5
//		QbSdk.initX5Environment(BaseApplication.getInstance(), null);
	}

	public void watchRefIfNeed(Object watchedReference){
		if(refWatcher != null){
			refWatcher.watch(watchedReference);
		}
	}

	private RefWatcher refWatcher;

	private void registerActivityLifecycleCallbacks(){
		registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks(){
			boolean isForeground = false;

			@Override
			public void onActivityResumed(final Activity activity) {
				if (!isForeground) {
					L.debug("app切回到前台");
					isForeground = true;
				}
			}

			@Override
			public void onActivityStopped(Activity activity) {
				if (!AppUtils.isAppOnForeground()) {
					L.debug("app切到后台");
					isForeground = false;
					//如果开启了视频播放，关闭；
					VideoPlayService.stop();
				}
			}

			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

			}

			@Override
			public void onActivityStarted(Activity activity) {

			}

			@Override
			public void onActivityPaused(Activity activity) {

			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

			}

			@Override
			public void onActivityDestroyed(Activity activity) {

			}
		});
	}
}
