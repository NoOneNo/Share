package com.hengye.share.ui.support.textspan;

public class TopicUrlOnTouchListener extends SimpleLinkOnTouchListener{

    private static TopicUrlOnTouchListener mInstance = new TopicUrlOnTouchListener();

    public static TopicUrlOnTouchListener getInstance(){
        return mInstance;
    }

    @Override
    public int getPressColor() {
        return 0xFFFF4081;
    }
}
