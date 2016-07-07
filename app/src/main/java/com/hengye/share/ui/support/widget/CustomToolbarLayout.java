package com.hengye.share.ui.support.widget;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;
import android.view.WindowManager;

/**
 * Created by yuhy on 16/7/6.
 */
public class CustomToolbarLayout extends CollapsingToolbarLayout {

    public CustomToolbarLayout(Context context) {
        this(context, null);
    }

    public CustomToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Set whether the content scrim and/or status bar scrim should be shown or not. Any change
     * in the vertical scroll may overwrite this value. Any visibility change will be animated if
     * this view has already been laid out.
     *
     * @param shown whether the scrims should be shown
     *
     * @see #getStatusBarScrim()
     * @see #getContentScrim()
     */
    boolean mScrimsShownFlag = true;
    public void setScrimsShown(boolean shown) {
        super.setScrimsShown(shown);
        if(mScrimsShownFlag == shown){
            return;
        }else{
            mScrimsShownFlag = shown;
        }
        if(getContext() instanceof Activity) {
            if (mScrimsShownFlag) {
                ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

}
