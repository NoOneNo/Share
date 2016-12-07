package com.hengye.share.ui.widget.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.hengye.share.module.util.encapsulation.view.listener.OnScrollChangeCompatListener;

import java.util.ArrayList;

/**
 * Created by yuhy on 2016/12/7.
 */

public class ObservableScrollView extends ScrollView {

    public ObservableScrollView(Context context) {
        this(context, null);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        for(OnScrollChangeCompatListener onScrollChangeListener : mOnScrollChangeListeners){
            onScrollChangeListener.onScrollChange(this, x, y, oldx, oldy);
        }
    }

    public ArrayList<OnScrollChangeCompatListener> mOnScrollChangeListeners = new ArrayList<>();

    public void addOnScrollChangeListener(OnScrollChangeCompatListener onScrollChangeListener){
        mOnScrollChangeListeners.add(onScrollChangeListener);
    }

    public void removeOnScrollChangeListener(OnScrollChangeCompatListener onScrollChangeListener){
        mOnScrollChangeListeners.remove(onScrollChangeListener);
    }
}
