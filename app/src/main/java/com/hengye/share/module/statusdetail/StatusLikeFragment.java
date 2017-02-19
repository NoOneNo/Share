package com.hengye.share.module.statusdetail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.hengye.floatingactionbutton.FloatingActionsMenu;
import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.profile.UserListAdapter;
import com.hengye.share.module.status.ShareLoadDataCallbackFragment;
import com.hengye.share.module.util.encapsulation.base.NumberPager;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.fab.FabAnimator;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.handler.StatusNumberPager;
import com.hengye.share.util.handler.StatusRefreshIdHandler;

public class StatusLikeFragment extends ShareLoadDataCallbackFragment<UserInfo>
        implements StatusLikeContract.View {

    public static StatusLikeFragment newInstance(Status status) {
        StatusLikeFragment fragment = new StatusLikeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    StatusLikeContract.Presenter mPresenter;
    UserListAdapter mAdapter;
    NumberPager mPager;
    Status mStatus;
    TabLayout.Tab mTab;

    @Override
    public int getLoadingResId() {
        return R.layout.state_loading_top;
    }

    @Override
    public int getEmptyResId() {
        return R.layout.state_empty_top;
    }

    @Override
    public int getNoNetworkResId() {
        return R.layout.state_no_network_top;
    }

    @Override
    public int getServiceErrorResId() {
        return R.layout.state_service_error_top;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mStatus = (Status) bundle.getSerializable("status");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAdapter(mAdapter = new UserListAdapter(getContext()));
        setPager(mPager = new StatusNumberPager());
        setDataHandler(new StatusRefreshIdHandler<>(mAdapter));
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
        mPresenter.listStatusLike(true, mStatus.getId(), mPager.getFirstPage(), mPager.getPageSize());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.listStatusLike(false, mStatus.getId(), mPager.getNextPage(), mPager.getPageSize());
    }

    @Override
    public void onLoadStatusLikeCount(long count) {
        if (mTab != null) {
            String str = ResUtil.getString(R.string.label_status_attitude_number,
                    DataUtil.getCounter(count));
            mTab.setText(str);
        }
    }
}
