package com.hengye.share.module.util.encapsulation.view.listener;

/**
 * Created by yuhy on 2016/11/1.
 */

/**
 * 滑动到顶部和底部的监听事件，子类实现如何滑动到顶部和底部
 */
public interface OnScrollToTopAndBottomListener {

    void onScrollToTop(boolean isSmooth);

    void onScrollToBottom(boolean isSmooth);
}
