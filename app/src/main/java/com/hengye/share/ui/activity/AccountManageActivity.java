package com.hengye.share.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.listener.OnItemLongClickListener;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountManageActivity extends BaseActivity implements AccountManageMvpView, DialogInterface.OnClickListener {

    public final static int ACCOUNT_CHANGE = 5;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_account_manage;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        addPresenter(mPresenter = new AccountManagePresenter(this));
        initView();
    }

    private AccountManagePresenter mPresenter;
    private AccountManageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Dialog mLogoutDialog;

    private User mSelectAccountOriginal;
    private boolean mHasSelectOriginalAccount;
    private int mSelectAccountNowIndex, mSelectAccountLongClickIndex;
    private View mSelectAccountBtn;

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new AccountManageAdapter(this, new ArrayList<User>(), new AccountManageCallBack()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == mSelectAccountNowIndex) {
                    return;
                }
                if (mSelectAccountBtn != null) {
                    mSelectAccountBtn.setVisibility(View.GONE);
                }
                mSelectAccountBtn = view.findViewById(R.id.btn_check);
                if (mSelectAccountBtn != null) {
                    mSelectAccountBtn.setVisibility(View.VISIBLE);
                }
                mSelectAccountNowIndex = position;
            }
        });

        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                mSelectAccountLongClickIndex = position;
                mLogoutDialog.show();
                return false;
            }
        });

        View logout = findViewById(R.id.tv_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(mAdapter.getItem(mSelectAccountNowIndex));
            }
        });

        mLogoutDialog = DialogBuilder.getItemDialog(this, this, getString(R.string.label_account_logout));
        mPresenter.loadUsers();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
            default:
                logout(mAdapter.getItem(mSelectAccountLongClickIndex));
                break;
        }
    }

    private void logout(User user) {
        if (user != null) {
            User accountSelectNow = mAdapter.getItem(mSelectAccountNowIndex);
            UserUtil.deleteUser(user);
            mAdapter.removeItem(user);
            //如果注销的是当前用户
            if (user.equals(accountSelectNow)) {
                if (!mAdapter.isEmpty()) {
                    mRecyclerView.getChildAt(0).performClick();
                } else {
                    mSelectAccountNowIndex = -1;
                }
            } else {
                mSelectAccountNowIndex = mAdapter.getItemPosition(accountSelectNow);
            }
        }
    }

    @Override
    public void loadSuccess(List<User> data, int currentUserIndex) {
        if (!mHasSelectOriginalAccount) {
            mHasSelectOriginalAccount = true;
            if (0 <= currentUserIndex && currentUserIndex < data.size()) {
                mSelectAccountOriginal = data.get(currentUserIndex);
            }
        }
        mSelectAccountNowIndex = currentUserIndex;
        mAdapter.refresh(data);
    }

    @Override
    public void loadFail() {
        if (!mHasSelectOriginalAccount) {
            mHasSelectOriginalAccount = true;
        }
    }

    private boolean isChangeAccount(User original, User now) {
        if (original == null && now == null) {
            return false;
        } else if (original == null || now == null) {
            return true;
        } else {
            return !original.equals(now);
        }
    }

    @Override
    public void finish() {
        User user = mAdapter.getItem(mSelectAccountNowIndex);
        if (isChangeAccount(mSelectAccountOriginal, user)) {
            setResult(Activity.RESULT_OK);
            UserUtil.changeCurrentUser(user);
        }
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO && resultCode == Activity.RESULT_OK) {
            mPresenter.loadUsers();
        }
    }

    public class AccountManageCallBack {
        public int getAccountSelectIndex() {
            return mSelectAccountNowIndex;
        }

        public void setAccountSelectBtn(View v) {
            mSelectAccountBtn = v;
        }
    }

    public static Dialog getLoginDialog(final Context context){
        SimpleTwoBtnDialog stbd = new SimpleTwoBtnDialog();
        stbd.setContent(R.string.label_to_login);
        stbd.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, AccountManageActivity.class));
            }
        });

        return stbd.create(context);
    }
}
