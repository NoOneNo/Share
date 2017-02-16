package com.hengye.share.module.search;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.base.ShareRecyclerFragment;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.topic.TopicAdapter;
import com.hengye.share.module.util.FragmentActivity;
import com.hengye.share.module.util.encapsulation.base.DefaultDataHandler;
import com.hengye.share.module.util.encapsulation.base.NumberPager;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.handler.TopicNumberPager;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends ShareRecyclerFragment<Topic>
        implements SearchContract.View, View.OnClickListener {

    private SearchUserBriefAdapter mUserAdapter;
    private TopicAdapter mTopicAdapter;
    private NumberPager mPager;
    private SearchContract.Presenter mPresenter;
    private View mHeader, mUserListContent, mUserListEmpty, mStatusEmpty;
    private String mContent;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        showLoading();
    }

    private void initView() {
        setAdapter(mTopicAdapter = new TopicAdapter(getContext(), new ArrayList<Topic>(), getRecyclerView()));
        setDataHandler(new DefaultDataHandler<>(mTopicAdapter));
        setPager(mPager = new TopicNumberPager(1, 15));
        setRefreshEnable(false);
        mPresenter = new SearchPresenter(this);
        initHeader();
    }


    private void initHeader() {
        mHeader = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_header, getRecyclerView(), false);
        mUserListContent = mHeader.findViewById(R.id.user_list_content);
        mUserListEmpty = mHeader.findViewById(R.id.empty_user_list);
        mStatusEmpty = mHeader.findViewById(R.id.empty_status);
        mTopicAdapter.setHeader(mHeader);

        setTitle(R.id.item_user_list_title, R.string.label_search_user);
        setTitle(R.id.item_status_list_title, R.string.label_search_topic);

        RecyclerView userRecycler = (RecyclerView) mHeader.findViewById(R.id.recycler_view_user);
        userRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        userRecycler.setAdapter(mUserAdapter = new SearchUserBriefAdapter(getContext(), new ArrayList<UserInfo>()));
        userRecycler.setItemAnimator(new DefaultItemAnimator());
        mUserAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                View avatar = view.findViewById(R.id.iv_avatar);
                PersonalHomepageActivity.start(getContext(), avatar, mUserAdapter.getItem(position));
            }
        });
    }

    private void setTitle(@IdRes int titleViewId, @StringRes int titleContentId) {
        View title = mHeader.findViewById(titleViewId);
        TextView header = (TextView) title.findViewById(R.id.tv_headline);
        header.setText(titleContentId);
        title.setOnClickListener(this);
    }

    public void search(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToast(R.string.label_search_content_is_null);
            return;
        }

        ViewUtil.hideKeyBoard(getActivity());
        mContent = content.trim();
        onRefresh();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.item_user_list_title) {
            if (!mUserAdapter.isEmpty()) {
                startActivity(
                        FragmentActivity.getStartIntent(getContext(),
                                SearchUserFragment.class,
                                SearchUserFragment.getStartBundle(mContent,
                                        (ArrayList<UserInfo>) mUserAdapter.getData()))
                );
            }
        }
    }

    @Override
    public void onLoadSearchUsers(List<UserInfo> userInfos) {
        if (!CommonUtil.isEmpty(userInfos)) {
            mUserListEmpty.setVisibility(View.GONE);
            mUserListContent.setVisibility(View.VISIBLE);
            mUserAdapter.refresh(userInfos);
        } else {
            mUserListEmpty.setVisibility(View.VISIBLE);
            mUserListContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mStatusEmpty.setVisibility(View.GONE);
        mPresenter.searchWBContent(mContent, true, mPager.getFirstPage(), mPager.getPageSize());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.searchWBContent(mContent, false, mPager.getNextPage(), mPager.getPageSize());
    }

    @Override
    public void showEmpty() {
        showContent();
        mStatusEmpty.setVisibility(View.VISIBLE);
    }
}
