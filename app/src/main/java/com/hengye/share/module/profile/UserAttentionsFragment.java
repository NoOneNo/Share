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
public class UserAttentionsFragment extends UserListFragment {

    public static void start(Context context, String uid){
        context.startActivity(FragmentActivity.getStartIntent(context, UserAttentionsFragment.class, UserListFragment.getStartBundle(uid)));
    }

    @Override
    public boolean setToolBar() {
        return true;
    }

    @Override
    public String getTitle() {
        return ResUtil.getString(R.string.title_activity_attention_list);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startRefresh();
    }

    @Override
    public void onRefresh() {
        getPresenter().loadWBAttentions(true);
    }

    @Override
    public void onLoad() {
        getPresenter().loadWBAttentions(false);
    }
}
