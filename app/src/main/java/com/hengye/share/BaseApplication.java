package com.hengye.share;

import android.app.Application;

import com.hengye.volleyplus.toolbox.RequestManager;

public class BaseApplication extends Application{

	@Override
	public void onCreate(){
		super.onCreate();
		
		init();
	}
	
	private void init(){
		RequestManager.init(this);
	}
}
