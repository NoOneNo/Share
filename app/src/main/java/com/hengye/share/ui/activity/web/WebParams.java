package com.hengye.share.ui.activity.web;

import java.io.Serializable;


public class WebParams implements Serializable{
	private static final long serialVersionUID = 6917526068224464289L;
	
	private String url;
	private int topBarStyle;
	private String title;
	private String finishedJavascript;

	public WebParams(String url) {
		
		this.url = url;
	}

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTopBarStyle() {
		return topBarStyle;
	}

	public void setTopBarStyle(int topBarStyle) {
		this.topBarStyle = topBarStyle;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFinishedJavascript() {
		return finishedJavascript;
	}

	public void setFinishedJavascript(String finishedJavascript) {
		this.finishedJavascript = finishedJavascript;
	}

//	public void setToIntent(Intent intent){
//		intent.putExtra(SwapHandle.PARAMS_TAG, this);
//	}
	
	
}