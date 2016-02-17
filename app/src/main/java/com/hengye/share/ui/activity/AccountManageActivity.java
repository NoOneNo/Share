package com.hengye.share.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.AccountManageAdapter;
import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.mvpview.AccountManageMvpView;
import com.hengye.share.ui.presenter.AccountManagePresenter;

import java.util.ArrayList;
import java.util.List;

public class AccountManageActivity extends BaseActivity implements AccountManageMvpView{

    public final static int ACCOUNT_CHANGE = 5;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_account_manage;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);

        setupPresenter(mPresenter = new AccountManagePresenter(this));
        initView();
    }

    private AccountManagePresenter mPresenter;
    private AccountManageAdapter mAdapter;
    private View mLogout;

    private void initView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new AccountManageAdapter(this, new ArrayList<User>()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mLogout = findViewById(R.id.tv_logout);

//        mPresenter.loadUsers();
    }

    @Override
    public void loadSuccess(List<User> data) {
        mAdapter.refresh(data);
    }

    @Override
    public void loadFail() {

    }
}
