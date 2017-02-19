package com.hengye.share.module.search;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.base.ShareRecyclerFragment;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.profile.UserAttentionContract;
import com.hengye.share.module.profile.UserAttentionPresenter;
import com.hengye.share.module.util.encapsulation.base.DefaultDataHandler;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.recyclerview.DividerItemDecoration;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.handler.StatusNumberPager;

import java.util.ArrayList;

public class SearchUserFragment extends ShareRecyclerFragment<UserInfo>
        implements SearchUserContract.View, UserAttentionContract.View {

    public static Bundle getStartBundle(String keywords, ArrayList<UserInfo> userInfos) {
        Bundle bundle = new Bundle();
        bundle.putString("keywords", keywords);
        bundle.putSerializable("userInfos", userInfos);
        return bundle;
    }

    private SearchUserAdapter mAdapter;
    private String mKeywords;
    private ArrayList<UserInfo> mUserInfos;
    private SearchUserPresenter mPresenter;
    private UserAttentionContract.Presenter mUserAttentionPresenter;
    private StatusNumberPager mPager;
    private boolean mRequesting = false;

    @Override
    public String getTitle() {
        return ResUtil.getString(R.string.title_activity_search_user);
    }

    @Override
    protected boolean isShowScrollbars() {
        return true;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        super.handleBundleExtra(bundle);
        mKeywords = bundle.getString("keywords");
        mUserInfos = (ArrayList<UserInfo>) bundle.getSerializable("userInfos");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRecyclerView().addItemDecoration(new DividerItemDecoration(getContext()));
        setAdapter(mAdapter = new SearchUserAdapter(getContext(), mUserInfos));
        setPager(mPager = new StatusNumberPager(1, 15));
        //已经存在数据
        mPager.handlePage(true);
        setDataHandler(new DefaultDataHandler<>(mAdapter));
        mPresenter = new SearchUserPresenter(this);
        mUserAttentionPresenter = new UserAttentionPresenter(this);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = view.getId();
                if(id == R.id.layout_attention){
                    if(!mRequesting) {
                        mUserAttentionPresenter.followUser(mAdapter.getItem(position));
                    }
                }else {
                    PersonalHomepageActivity.start(getContext(),
                            view.findViewById(R.id.iv_avatar),
                            mAdapter.getItem(position));
                }
            }
        });

        if(mAdapter.getBasicItemCount() >= mPager.getPageSize()){
            setLoadEnable(true);
        }

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.searchWBUser(mKeywords, true, mPager.getFirstPage(), mPager.getPageSize());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.searchWBUser(mKeywords, false, mPager.getNextPage(), mPager.getPageSize());
    }

    @Override
    public void onFollowStart() {
        mRequesting = true;
    }

    @Override
    public void onFollowComplete(int taskState) {
        mRequesting = false;
        TaskState.toastFailState(taskState);
    }

    @Override
    public void onFollowSuccess(UserInfo userInfo) {
        mAdapter.notifyItemChanged(userInfo);
    }
}
