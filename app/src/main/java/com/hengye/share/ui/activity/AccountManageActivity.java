package com.hengye.share.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountManageActivity extends BaseActivity implements AccountManageMvpView, DialogInterface.OnClickListener{

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
    private RecyclerView mRecyclerView;
    private Dialog mLogoutDialog;

    private int mAccountSelectOriginalIndex = -1;
    private int mAccountSelectNowIndex, mAccountSelectLongClickIndex;
    private View mAccountSelectBtn;

    private void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new AccountManageAdapter(this, new ArrayList<User>(), new AccountManageCallBack()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

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

        mAdapter.setOnItemLongClickListener(new ViewUtil.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                mAccountSelectLongClickIndex = position;
                mLogoutDialog.show();
                return false;
            }
        });

        View logout = findViewById(R.id.tv_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(mAdapter.getItem(mAccountSelectNowIndex));
            }
        });

        mLogoutDialog = DialogBuilder.getItemDialog(this, this, getString(R.string.label_account_logout));
        mPresenter.loadUsers();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case 0:
            default:
                logout(mAdapter.getItem(mAccountSelectLongClickIndex));
                break;
        }
    }

    private void logout(User user){
        if(user != null){
            UserUtil.deleteUser(user);
            mAdapter.removeItem(mAccountSelectLongClickIndex);
            //如果注销的是当前用户
            if(mAccountSelectNowIndex == mAccountSelectLongClickIndex){
                if(!mAdapter.isEmpty()){
                    mRecyclerView.getChildAt(0).performClick();
                }
            }
        }
    }

    @Override
    public void loadSuccess(List<User> data, int currentUserIndex) {
        if(mAccountSelectOriginalIndex == -1){
            mAccountSelectOriginalIndex = currentUserIndex;
        }
        mAccountSelectNowIndex = currentUserIndex;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO && resultCode == Activity.RESULT_OK){
            mPresenter.loadUsers();
        }
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
