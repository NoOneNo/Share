package com.hengye.share.module.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.update.CheckUpdateMvpImpl;
import com.hengye.share.module.update.CheckUpdatePresenter;
import com.hengye.share.service.VideoPlayService;
import com.hengye.share.support.DefaultActivityLifecycleCallback;
import com.hengye.share.util.AppUtils;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ToastUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

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
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}


	private void init() {
//		startService(new Intent(this, ShareService.class));
		ourInstance = this;

		registerActivityLifecycleCallbacks(new DefaultActivityLifecycleCallback(){
			boolean isForeground = false;

			@Override
			public void onActivityResumed(final Activity activity) {
				super.onActivityResumed(activity);
				if (!isForeground) {
					L.debug("app切回到前台");
					isForeground = true;
				}
			}

			@Override
			public void onActivityStopped(Activity activity) {
				super.onActivityStopped(activity);
				if (!AppUtils.isAppOnForeground()) {
					L.debug("app切到后台");
					isForeground = false;
					//如果开启了视频播放，关闭；
					stopService(new Intent(BaseApplication.this, VideoPlayService.class));
				}
			}
		});


//		SkinManager.setup(this);
		//放到GuidanceActivity初始化
		//初始化volley
		RequestManager.init(this, null, MAX_NETWORK_CACHE_SIZE);
//		//初始化腾讯bugly
//		CrashReport.initCrashReport(BaseApplication.getInstance(), "900019432", false);
//		//初始化腾讯x5
//		QbSdk.initX5Environment(BaseApplication.getInstance(), null);
	}
}
