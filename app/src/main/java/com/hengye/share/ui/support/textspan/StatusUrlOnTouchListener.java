package com.hengye.share.ui.support.textspan;

import com.hengye.share.R;
import com.hengye.share.util.ResUtil;

public class StatusUrlOnTouchListener extends SimpleLinkOnTouchListener{

    private static StatusUrlOnTouchListener mInstance = new StatusUrlOnTouchListener();

    public static StatusUrlOnTouchListener getInstance(){
        return mInstance;
    }

    @Override
    public int getPressColor() {
        return ResUtil.getColor(R.color.status_press_bg);
    }
}
