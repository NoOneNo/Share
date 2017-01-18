package com.hengye.share.module.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.base.DefaultDataHandler;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.base.ShareRecyclerFragment;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
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
        setAdapter(mAdapter = new UserListAdapter(getContext()));
        setDataHandler(new DefaultDataHandler<>(mAdapter));

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UserInfo userInfo = mAdapter.getItem(position);
                View avatar = view.findViewById(R.id.iv_avatar);
                PersonalHomepageActivity.start(getContext(), avatar, userInfo);
            }
        });
    }

    public UserListPresenter getPresenter() {
        return mPresenter;
    }

}
