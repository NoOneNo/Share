package com.hengye.share.module.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.base.DefaultDataHandler;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.base.ShareRecyclerFragment;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.recyclerview.DividerItemDecoration;
import com.hengye.share.util.UserUtil;

public class UserListFragment extends ShareRecyclerFragment<UserInfo> implements UserListContract.View {

    public static Bundle getStartBundle(String uid) {
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        return bundle;
    }

    UserListPresenter mPresenter;
    UserListAdapter mAdapter;

    @Override
    protected boolean isShowScrollbars() {
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String uid = getArguments().getString("uid");
        if(uid == null){
            uid = UserUtil.getUid();
        }
        getRecyclerView().addItemDecoration(new DividerItemDecoration(getContext()));
        setAdapter(mAdapter = new UserListAdapter(getContext()));
        setDataHandler(new DefaultDataHandler<>(mAdapter));
        mPresenter = new UserListPresenter(this, uid);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PersonalHomepageActivity.start(getContext(),
                        view.findViewById(R.id.iv_avatar),
                        mAdapter.getItem(position));
            }
        });
    }

    public UserListPresenter getPresenter() {
        return mPresenter;
    }

}
