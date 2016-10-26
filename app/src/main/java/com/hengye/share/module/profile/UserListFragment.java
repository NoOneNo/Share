package com.hengye.share.module.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.handler.data.DefaultDataHandler;
import com.hengye.share.handler.data.base.DataHandler;
import com.hengye.share.model.Select;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.paging.RecyclerRefreshFragment;
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;

import java.util.List;

public class UserListFragment extends RecyclerRefreshFragment<Select<UserInfo>> implements UserListMvpView {

    public static Bundle getStartBundle(String uid) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        return bundle;
    }

    UserListPresenter mPresenter;
    DefaultDataHandler<Select<UserInfo>> mDataHandler;
    UserListAdapter mAdapter;

    @Override
    public int getContentResId() {
        return R.layout.fragment_recycler_refresh_vertical;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String uid = getArguments().getString("uid");
        if(uid == null){
            uid = UserUtil.getUid();
        }
        mPresenter = new UserListPresenter(this, uid);
        addPresenter(mPresenter);
        mDataHandler = new DefaultDataHandler<>(mAdapter = new UserListAdapter(getContext()));
        setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserInfo userInfo = mAdapter.getItem(position).getTarget();
                View avatar = view.findViewById(R.id.iv_avatar);
                PersonalHomepageActivity.start(getContext(), avatar, userInfo);
            }
        });
    }

    @Override
    public DataHandler<Select<UserInfo>> getDataHandler() {
        return mDataHandler;
    }

    @Override
    public void showUserListSuccess(List<UserInfo> data, boolean isRefresh) {
        handleData(isRefresh, Select.get(data));
    }

    @Override
    public void onTaskComplete(boolean isRefresh, boolean isSuccess) {
        if(!isSuccess){
            ToastUtil.showLoadErrorToast();
        }
        setTaskComplete(isSuccess);
    }

    @Override
    public void canLoadMore(boolean loadEnable) {
        setLoadEnable(loadEnable);
    }

    public UserListPresenter getPresenter() {
        return mPresenter;
    }

}
