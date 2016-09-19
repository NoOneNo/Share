package com.hengye.share.module.groupmanage;

import android.support.v4.app.Fragment;

import com.hengye.share.module.base.BaseFragmentActivity;

public class GroupManageActivity extends BaseFragmentActivity{

    public final static int GROUP_UPDATE = 11;

    @Override
    protected Fragment getFragment() {
        GroupManageFragment fragment =  new GroupManageFragment();
        getActivityHelper().registerActivityActionInterceptListener(fragment);
        return fragment;
    }
}
