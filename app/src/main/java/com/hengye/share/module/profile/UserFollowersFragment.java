package com.hengye.share.module.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.util.FragmentActivity;
import com.hengye.share.util.ResUtil;

/**
 * Created by yuhy on 16/8/22.
 */
public class UserFollowersFragment extends UserListFragment {

    public static void start(Context context, String uid){
        context.startActivity(FragmentActivity.getStartIntent(context, UserFollowersFragment.class, UserListFragment.getStartBundle(uid)));
    }

    @Override
    public boolean setToolBar() {
        return true;
    }

    @Override
    public String getTitle() {
        return ResUtil.getString(R.string.title_activity_follower_list);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        getPresenter().loadWBFollowers(true);
    }

    @Override
    public void onLoad() {
        getPresenter().loadWBFollowers(false);
    }
}
