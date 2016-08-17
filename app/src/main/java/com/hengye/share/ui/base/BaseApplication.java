package com.hengye.share.ui.base;

import android.app.Application;
import android.content.Intent;

import com.hengye.share.service.ShareService;
import com.hengye.share.util.RequestManager;
import com.hengye.skinloader.support.SkinManager;
import com.tencent.bugly.crashreport.CrashReport;

public class BaseApplication extends Application{

	private static BaseApplication ourInstance;

	public final static int MAX_NETWORK_CACHE_SIZE = 200 * 1024 * 1024;

	@Override
	public void onCreate(){
		super.onCreate();
		
		init();
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
