package com.hengye.swiperefresh.listener;

/**
 * Created by yuhy on 16/4/29.
 */
public class SwipeListener {

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    public interface OnRefreshListener {
        void onRefresh();
    }

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a load more should implement this interface.
     */
    public interface OnLoadListener {
        void onLoad();
    }
}
