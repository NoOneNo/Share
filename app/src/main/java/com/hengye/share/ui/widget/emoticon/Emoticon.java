package com.hengye.share.ui.widget.emoticon;

/**
 * Created by yuhy on 2017/2/8.
 */

public class Emoticon {

    private String key;

    private int resId;

    public Emoticon(String key, int resId) {
        this.key = key;
        this.resId = resId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
