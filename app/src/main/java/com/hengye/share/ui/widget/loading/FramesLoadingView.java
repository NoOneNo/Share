package com.hengye.share.ui.widget.loading;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.hengye.share.R;

public class FramesLoadingView extends ImageView {

    public FramesLoadingView(Context context) {
        super(context);
        init(context);
    }

    public FramesLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FramesLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected AnimationDrawable mLoadingDrawable;

    protected void init(Context context){

        mLoadingDrawable = (AnimationDrawable) context.getDrawable(R.drawable.loading_share);
        setImageDrawable(mLoadingDrawable);
        start();
    }

    public void start(){
        mLoadingDrawable.start();
    }

    public void stop(){
        mLoadingDrawable.stop();
    }

    public boolean isRunning(){
        return mLoadingDrawable.isRunning();
    }
}
