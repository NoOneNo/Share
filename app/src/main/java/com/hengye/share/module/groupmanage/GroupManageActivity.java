package com.hengye.share.module.groupmanage;

import android.support.v4.app.Fragment;

import com.hengye.share.module.base.BaseFragmentActivity;

public class GroupManageActivity extends BaseFragmentActivity{

    @Override
    protected Fragment createFragment() {
        GroupManageFragment fragment =  new GroupManageFragment();
        getActivityHelper().registerActivityActionInterceptListener(fragment);
        return fragment;
    }
}
