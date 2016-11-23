package com.hengye.share.module.util.encapsulation.view.listener;

import android.view.View;

import com.hengye.share.util.ViewUtil;

/**
 * Created by yuhy on 2016/11/23.
 */

public class OnLongClickVibrateListener implements View.OnLongClickListener {

    @Override
    public boolean onLongClick(View v) {
        ViewUtil.vibrate(v);
        return false;
    }
}
