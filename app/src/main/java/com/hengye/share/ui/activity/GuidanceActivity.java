package com.hengye.share.ui.activity;

import android.os.Bundle;

import com.hengye.share.ui.base.BaseActivity;

public class GuidanceActivity extends BaseActivity{

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        startActivity(TopicActivity.class);
        finish();
    }
}
