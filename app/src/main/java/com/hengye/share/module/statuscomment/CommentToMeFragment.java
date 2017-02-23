package com.hengye.share.module.statuscomment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.model.Status;
import com.hengye.share.module.status.ShareLoadDataCallbackFragment;
import com.hengye.share.module.status.StatusNotifyAdapter;
import com.hengye.share.module.status.StatusPresenter;
import com.hengye.share.module.util.encapsulation.base.DataType;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.handler.StatusAdapterIdPager;
import com.hengye.share.util.handler.StatusIdHandler;

import java.util.ArrayList;

public class CommentToMeFragment extends ShareLoadDataCallbackFragment<Status> implements CommentContract.View {

    public static Bundle getBundle(CommentPresenter.CommentGroup commentGroup) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("commentGroup", commentGroup);
        return bundle;
    }

    public static CommentToMeFragment newInstance(CommentPresenter.CommentGroup commentGroup) {
        CommentToMeFragment fragment = new CommentToMeFragment();
        fragment.setArguments(getBundle(commentGroup));
        return fragment;
    }

    CommentPresenter mPresenter;
    CommentPresenter.CommentGroup commentGroup;

    StatusAdapterIdPager mStatusPager;
    StatusNotifyAdapter mAdapter;

    @Override
    public StatusNotifyAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        commentGroup = (CommentPresenter.CommentGroup) bundle.getSerializable("commentGroup");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter = new StatusNotifyAdapter(getContext(), new ArrayList<Status>(), getRecyclerView()));
        setPager(mStatusPager = new StatusAdapterIdPager(mAdapter));
        setDataHandler();
        mAdapter.setShowDeleteStatusOption(true);

        mPresenter = new CommentPresenter(this, commentGroup);
        showLoading();
        markLazyLoadPreparedAndLazyLoadIfCan();
    }

    protected void setDataHandler() {
        setDataHandler(new StatusIdHandler<>(mAdapter));
    }

    @Override
    protected void onLazyLoad() {
        if (!UserUtil.isUserEmpty()) {
            mPresenter.loadWBComment();
        } else {
            showEmpty();
        }
    }

    @Override
    public void onTaskStart() {
        super.onTaskStart();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.loadWBComment(mStatusPager.getFirstPage(), true);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadWBComment(mStatusPager.getNextPage(), false);
    }

    @Override
    public void handleDataType(int type) {
        super.handleDataType(type);
        if (DataType.hasNewData(type)) {
            mPresenter.saveData(mAdapter.getData());
        }
    }
}
