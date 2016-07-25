package com.hengye.share.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.util.ViewUtil.OnDoubleClickListener;

public class CommonToolBar extends Toolbar {

    long mLastClickTime;
    OnDoubleClickListener mOnDoubleClickListener;

    public CommonToolBar(Context context) {
        this(context, null);
    }

    public CommonToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public CommonToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public void init() {
        this.setNavigationIcon(R.drawable.btn_back);
//        this.setNavigationOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getContext() instanceof Activity) {
//                    ((Activity) getContext()).onBackPressed();
//                }
//            }
//        });
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean handler = super.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (this.mLastClickTime != 0L && System.currentTimeMillis() - this.mLastClickTime <= 500L) {
                performDoubleClick();
            }
            this.mLastClickTime = System.currentTimeMillis();
        }
        return handler;
    }

    public void performDoubleClick() {
        if (getOnDoubleClickListener() != null) {
            getOnDoubleClickListener().onDoubleClick(this);
        }
    }

    public OnDoubleClickListener getOnDoubleClickListener() {
        return mOnDoubleClickListener;
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.mOnDoubleClickListener = onDoubleClickListener;
    }
}
