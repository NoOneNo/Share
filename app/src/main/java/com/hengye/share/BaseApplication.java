package com.hengye.share;

import android.app.Application;

import com.hengye.share.util.CrashHandler;
import com.hengye.share.util.SPUtil;
import com.hengye.volleyplus.toolbox.RequestManager;

public class BaseApplication extends Application{

	@Override
	public void onCreate(){
		super.onCreate();
		
		init();
	}
	
	private void init(){

		SPUtil.getInstance().init(getApplicationContext());
//		CrashHandler.getInstance().init(getApplicationContext());
		RequestManager.init(this);
	}
}
