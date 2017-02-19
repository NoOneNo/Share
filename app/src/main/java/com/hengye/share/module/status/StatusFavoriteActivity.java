package com.hengye.share.module.status;

import android.os.Bundle;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;

/**
 * Created by yuhy on 2016/11/10.
 */

public class StatusFavoriteActivity extends BaseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, StatusFavoriteFragment.newInstance())
                .commit();
    }
}
