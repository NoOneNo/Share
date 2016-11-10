package com.hengye.share.module.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.hengye.share.service.VideoPlayService;
import com.hengye.share.support.DefaultActivityLifecycleCallback;
import com.hengye.share.util.AppUtils;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ToastUtil;
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
		long start = System.currentTimeMillis();
		init();
		long end = System.currentTimeMillis();

		L.debug("application onCreate consume : {}", end - start);

		registerActivityLifecycleCallbacks(new DefaultActivityLifecycleCallback(){

			boolean isForeground = false;

			@Override
			public void onActivityResumed(final Activity activity) {
				super.onActivityResumed(activity);
				L.debug("app onResume()");
				if (!isForeground) {
					ToastUtil.showToast("切回到前台");
					isForeground = true;
				}
			}

			@Override
			public void onActivityStopped(Activity activity) {
				super.onActivityStopped(activity);
				if (!AppUtils.isAppOnForeground()) {
					isForeground = false;
					ToastUtil.showToast("切到后台");
				}
			}
		});
	}

	private void init() {

//		startService(new Intent(this, ShareService.class));

		ourInstance = this;

//		SkinManager.setup(this);
		RequestManager.init(this, null, MAX_NETWORK_CACHE_SIZE);

		CrashReport.initCrashReport(getApplicationContext(), "900019432", false);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
}
