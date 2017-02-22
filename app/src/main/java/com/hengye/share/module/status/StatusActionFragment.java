package com.hengye.share.module.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.ui.widget.recyclerview.DividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by yuhy on 2017/2/15.
 */

public class StatusActionFragment extends ShareLoadDataCallbackFragment<Status> implements StatusActionContract.View {

    protected StatusAdapter mAdapter;
    private StatusActionMvpImpl mStatusActionMvpImpl;
    private StatusActionContract.Presenter mStatusActionPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DividerItemDecoration decoration = new DividerItemDecoration(getResources().getDrawable(R.drawable.status_divider_light));
        decoration.ignoreLastItem(true);
        getRecyclerView().addItemDecoration(decoration);
        setAdapter(mAdapter = new StatusAdapter(getContext(), new ArrayList<Status>(), getRecyclerView()));
        mStatusActionPresenter = new StatusActonPresenter(this);
        mStatusActionMvpImpl = new StatusActionMvpImpl(this, mStatusActionPresenter);
        mAdapter.setStatusActionPresenter(mStatusActionPresenter);
    }

    @Override
    public void onLikeStatusComplete(Status status, int taskState) {
        mStatusActionMvpImpl.onLikeStatusComplete(status, taskState);
        mAdapter.updateItem(status);
    }

    @Override
    public void onCollectStatusComplete(Status status, int taskState) {
        mStatusActionMvpImpl.onCollectStatusComplete(status, taskState);
    }
}
