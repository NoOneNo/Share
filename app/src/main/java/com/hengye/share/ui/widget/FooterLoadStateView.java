package com.hengye.share.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.base.StateLayoutManager;

/**
 * Created by yuhy on 2016/12/14.
 */

public class FooterLoadStateView extends FrameLayout implements
        StateLayoutManager.OnFindStateViewListener{

    public static final int STATE_LOAD_MORE = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_LOAD_FAIL = 3;
    public static final int STATE_ENDING = 4;

    private StateLayoutManager stateLayoutManager;

    public FooterLoadStateView(Context context) {
        super(context);
        init();
    }

    public FooterLoadStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FooterLoadStateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        initStateLayoutManager();
    }

    private int getLoadMoreResId(){
        return R.layout.footer_load_more;
    }

    private int getLoadingResId(){
        return R.layout.footer_loading;
    }

    private int getLoadFailResId(){
        return R.layout.footer_load_fail;
    }

    private int getEndingResId(){
        return R.layout.footer_ending;
    }

    private void initStateLayoutManager(){
        stateLayoutManager = new StateLayoutManager(getContext(), this, true);
        stateLayoutManager.setViewResourceId(STATE_LOAD_MORE, getLoadMoreResId());
        stateLayoutManager.setViewResourceId(STATE_LOADING, getLoadingResId());
        stateLayoutManager.setViewResourceId(STATE_LOAD_FAIL, getLoadFailResId());
        stateLayoutManager.setViewResourceId(STATE_ENDING, getEndingResId());
        stateLayoutManager.setOnFindStateViewListener(this);
    }

    @Override
    public void onFindStateView(final View stateView, final int state) {
        if(onLoadStateClickListener != null) {
            stateView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoadStateClickListener.onLoadStateClick(stateView, state);
                }
            });
        }
    }

    private onLoadStateClickListener onLoadStateClickListener;

    public void setOnLoadStateClickListener(FooterLoadStateView.onLoadStateClickListener onLoadStateClickListener) {
        this.onLoadStateClickListener = onLoadStateClickListener;
    }

    public void showLoadMore(){
        stateLayoutManager.showState(STATE_LOAD_MORE);
    }

    public void showLoading(){
        stateLayoutManager.showState(STATE_LOADING);
    }

    public void showLoadFail(){
        stateLayoutManager.showState(STATE_LOAD_FAIL);
    }

    public void showEnding(){
        stateLayoutManager.showState(STATE_ENDING);
    }

    public int getCurrentState(){
        return stateLayoutManager.getCurrentState();
    }

    public void showState(int state){
        stateLayoutManager.showState(state);
    }

    public interface onLoadStateClickListener{
        void onLoadStateClick(View v, int state);
    }
}
