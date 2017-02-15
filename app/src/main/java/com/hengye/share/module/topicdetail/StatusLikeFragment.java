package com.hengye.share.module.topicdetail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.hengye.floatingactionbutton.FloatingActionsMenu;
import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.profile.UserListAdapter;
import com.hengye.share.module.topic.ShareLoadDataCallbackFragment;
import com.hengye.share.module.util.encapsulation.base.NumberPager;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.fab.FabAnimator;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.handler.TopicNumberPager;
import com.hengye.share.util.handler.TopicRefreshIdHandler;

public class StatusLikeFragment extends ShareLoadDataCallbackFragment<UserInfo>
        implements StatusLikeContract.View {

    public static StatusLikeFragment newInstance(Topic topic) {
        StatusLikeFragment fragment = new StatusLikeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("topic", topic);
        fragment.setArguments(bundle);
        return fragment;
    }

    StatusLikeContract.Presenter mPresenter;
    UserListAdapter mAdapter;
    NumberPager mPager;
    Topic mTopic;
    TabLayout.Tab mTab;

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mTopic = (Topic) bundle.getSerializable("topic");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAdapter(mAdapter = new UserListAdapter(getContext()));
        setPager(mPager = new TopicNumberPager());
        setDataHandler(new TopicRefreshIdHandler<>(mAdapter));
        mPresenter = new StatusLikePresenter(this);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PersonalHomepageActivity.start(getContext(),
                        view.findViewById(R.id.iv_avatar),
                        mAdapter.getItem(position));
            }
        });
        initView();
        showLoading();
        markLazyLoadPreparedAndLazyLoadIfCan();
    }

    private void initView() {
        setRefreshEnable(false);
        final TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab);
        if (tabLayout != null) {
            mTab = tabLayout.getTabAt(2);
        }

        final FloatingActionsMenu actionsMenu = (FloatingActionsMenu) getActivity().findViewById(R.id.fab);
        if (actionsMenu != null) {
            FabAnimator
                    .create(actionsMenu)
                    .attachToRecyclerView(getRecyclerView())
                    .setCustomAnimator(new FabAnimator.CustomAnimator() {
                        @Override
                        public int getViewHeight() {
                            return actionsMenu.getAddButtion().getHeight();
                        }
                    });
        }
    }

    @Override
    protected void onLazyLoad() {
        if (isShowLoading()) {
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.listStatusLike(true, mTopic.getId(), mPager.getFirstPage(), mPager.getPageSize());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.listStatusLike(false, mTopic.getId(), mPager.getNextPage(), mPager.getPageSize());
    }

    @Override
    public void onLoadStatusLikeCount(long count) {
        if (mTab != null) {
            String str = ResUtil.getString(R.string.label_topic_attitude_number,
                    DataUtil.getCounter(count));
            mTab.setText(str);
        }
    }
}
