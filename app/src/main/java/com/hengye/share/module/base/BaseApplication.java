package com.hengye.share.module.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
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
