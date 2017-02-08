package com.hengye.share.ui.widget.emoticon;

import android.support.annotation.DrawableRes;

/**
 * Created by yuhy on 2017/2/8.
 */

public class EmoticonPage {

    @EmoticonUtil.Type
    private String type;

    private @DrawableRes int iconResId;

    public EmoticonPage(@EmoticonUtil.Type String type, @DrawableRes int iconResId) {
        this.type = type;
        this.iconResId = iconResId;
    }

    @EmoticonUtil.Type
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @DrawableRes
    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
