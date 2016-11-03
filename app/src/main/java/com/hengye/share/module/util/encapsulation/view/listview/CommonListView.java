package com.hengye.share.module.util.encapsulation.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by yuhy on 2016/10/13.
 */

public class CommonListView extends ListView implements AbsListView.OnScrollListener{

    public CommonListView(Context context) {
        super(context);
        init();
    }

    public CommonListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CommonListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, 0);
        init();
    }

    public void init(){
        super.setOnScrollListener(this);
    }

    public ArrayList<OnScrollListener> mOnScrollListeners = new ArrayList<>();

    public void addOnScrollListener(OnScrollListener onScrollListener){
        mOnScrollListeners.add(onScrollListener);
    }

    public void removeOnScrollListener(OnScrollListener onScrollListener){
        mOnScrollListeners.remove(onScrollListener);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        for(OnScrollListener onScrollListener : mOnScrollListeners){
            onScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        for(OnScrollListener onScrollListener : mOnScrollListeners){
            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    /**
     * User {@link #addOnScrollListener(OnScrollListener)}
     * @param l
     */
    @Deprecated
    public void setOnScrollListener(OnScrollListener l) {
        addOnScrollListener(l);
    }
}
