package com.hengye.share.module.base;

import android.app.Application;

import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.tencent.bugly.crashreport.CrashReport;

public class BaseApplication extends Application{

	private static BaseApplication ourInstance;

	public final static int MAX_NETWORK_CACHE_SIZE = 200 * 1024 * 1024;

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
//		CrashHandler.getInstance().init(getApplicationContext());
		RequestManager.init(this, null, MAX_NETWORK_CACHE_SIZE);

		CrashReport.initCrashReport(getApplicationContext(), "900019432", false);
	}

	public static BaseApplication getInstance(){
		return ourInstance;
	}
}
