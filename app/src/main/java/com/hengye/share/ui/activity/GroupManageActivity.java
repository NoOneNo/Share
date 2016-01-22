package com.hengye.share.ui.activity;

import android.os.Bundle;

import com.hengye.share.R;
import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.mvpview.GroupManageMvpView;
import com.hengye.share.ui.presenter.GroupManagePresenter;
import com.hengye.share.util.L;

public class GroupManageActivity extends BaseActivity implements GroupManageMvpView{

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_group_manage;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);

        setupPresenter(mPresenter = new GroupManagePresenter(this));

        mPresenter.loadGroupList();
    }

    private GroupManagePresenter mPresenter;

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFail() {

    }

    @Override
    public void handleGroupList(WBGroups wbGroups) {
        L.debug("wbGroups : {}", wbGroups.toString());
    }
}
