package com.hengye.share.module.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hengye.share.R;
import com.hengye.share.util.ActivityUtils;

/**
 * Created by yuhy on 16/9/19.
 */
public abstract class BaseFragmentActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(ActivityUtils.FRAGMENT_ID);

        if (fragment == null) {
            fragment = getFragment();
            ActivityUtils.addFragmentToActivity(this,
                    fragment, ActivityUtils.FRAGMENT_ID);
        }

    }
    protected abstract Fragment getFragment();

}
