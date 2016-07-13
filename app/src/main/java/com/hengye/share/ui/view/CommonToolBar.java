package com.hengye.share.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.util.L;

public class CommonToolBar extends Toolbar {

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
        this.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).onBackPressed();
                }
            }
        });
    }

}
