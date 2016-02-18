package com.hengye.share.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.AccountManageAdapter;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.mvpview.AccountManageMvpView;
import com.hengye.share.ui.presenter.AccountManagePresenter;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;

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

    private int mAccountSelectOriginalIndex, mAccountSelectNowIndex;
    private View mAccountSelectBtn;

    private void initView(){
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new AccountManageAdapter(this, new ArrayList<User>(), new AccountManageCallBack()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == mAccountSelectNowIndex){
                    return;
                }
                if(mAccountSelectBtn != null){
                    mAccountSelectBtn.setVisibility(View.GONE);
                }
                mAccountSelectBtn = view.findViewById(R.id.btn_check);
                if(mAccountSelectBtn != null){
                    mAccountSelectBtn.setVisibility(View.VISIBLE);
                }
                mAccountSelectNowIndex = position;
            }
        });

        mLogout = findViewById(R.id.tv_logout);
        mPresenter.loadUsers();
    }

    @Override
    public void loadSuccess(List<User> data, int currentUserIndex) {
        mAccountSelectNowIndex = mAccountSelectOriginalIndex = currentUserIndex;
        mAdapter.refresh(data);
    }

    @Override
    public void loadFail() {

    }

    @Override
    public void onBackPressed() {
        if(mAccountSelectOriginalIndex != mAccountSelectNowIndex){
            User user = mAdapter.getItem(mAccountSelectNowIndex);
            if(user != null) {
                setResult(Activity.RESULT_OK);
                UserUtil.changeCurrentUser(user);
            }
        }
        super.onBackPressed();
    }

    public class AccountManageCallBack{
        public int getAccountSelectIndex(){
            return mAccountSelectNowIndex;
        }

        public void setAccountSelectBtn(View v){
            mAccountSelectBtn = v;
        }
    }
}
