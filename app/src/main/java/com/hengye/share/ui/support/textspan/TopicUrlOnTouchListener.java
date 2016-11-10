package com.hengye.share.ui.support.textspan;

import com.hengye.share.R;
import com.hengye.share.util.ResUtil;

public class TopicUrlOnTouchListener extends SimpleLinkOnTouchListener{

    private static TopicUrlOnTouchListener mInstance = new TopicUrlOnTouchListener();

    public static TopicUrlOnTouchListener getInstance(){
        return mInstance;
    }

    @Override
    public int getPressColor() {
        return ResUtil.getColor(R.color.topic_press_bg);
    }
}
