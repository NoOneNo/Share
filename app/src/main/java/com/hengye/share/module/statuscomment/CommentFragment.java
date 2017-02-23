package com.hengye.share.module.statuscomment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.StatusComment;
import com.hengye.share.module.status.ShareLoadDataCallbackFragment;
import com.hengye.share.module.util.encapsulation.base.DataType;
import com.hengye.share.ui.widget.recyclerview.DividerItemDecoration;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.handler.StatusAdapterIdPager;
import com.hengye.share.util.handler.StatusIdHandler;

import java.util.ArrayList;

public class CommentFragment extends ShareLoadDataCallbackFragment<StatusComment> implements CommentContract.View {

    public static Bundle getBundle(CommentPresenter.CommentGroup commentGroup) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("commentGroup", commentGroup);
        return bundle;
    }

    public static CommentFragment newInstance(CommentPresenter.CommentGroup commentGroup) {
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(getBundle(commentGroup));
        return fragment;
    }

    CommentPresenter mPresenter;
    CommentPresenter.CommentGroup commentGroup;

    StatusAdapterIdPager mStatusPager;
    CommentAdapter mAdapter;

    @Override
    public CommentAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        commentGroup = (CommentPresenter.CommentGroup) bundle.getSerializable("commentGroup");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DividerItemDecoration decoration = new DividerItemDecoration(getResources().getDrawable(R.drawable.status_divider_light));
        decoration.ignoreLastItem(true);
        getRecyclerView().addItemDecoration(decoration);
        setAdapter(mAdapter = new CommentAdapter(getContext(), new ArrayList<StatusComment>(), getRecyclerView()));
        setPager(mStatusPager = new StatusAdapterIdPager(mAdapter));
        setDataHandler(new StatusIdHandler<>(mAdapter));

        mPresenter = new CommentPresenter(this, commentGroup);
        showLoading();
        markLazyLoadPreparedAndLazyLoadIfCan();
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
