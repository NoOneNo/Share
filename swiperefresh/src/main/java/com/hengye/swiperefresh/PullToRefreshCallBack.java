package com.hengye.swiperefresh;

/**
 * Created by yuhy on 16/5/4.
 */
public interface PullToRefreshCallBack {

    /**
     * 当控件没有正在刷新时,按下会被调用
     */
    void onPressDown();

    /**
     * 当控件处于下拉状态,在可以刷新或者不能刷新的状态切换时,会调用
     * @param canRefresh
     */
    void onPullToRefresh(boolean canRefresh);

    /**
     * 当控件开始触发刷新时会调用
     */
    void onRefreshStart();

    /**
     * 当控件刷新完毕时会调用
     */
    void onRefreshComplete();
}
