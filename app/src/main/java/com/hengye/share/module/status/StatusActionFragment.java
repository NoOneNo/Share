package com.hengye.share.module.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.model.Status;

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
        setAdapter(mAdapter = new StatusAdapter(getContext(), new ArrayList<Status>(), getRecyclerView()));
        mStatusActionPresenter = new StatusActonPresenter(this);
        mStatusActionMvpImpl = new StatusActionMvpImpl(this, mStatusActionPresenter);
        mAdapter.setStatusActionPresenter(mStatusActionPresenter);
    }

    @Override
    public void onLikeStatusComplete(Status topic, int taskState) {
        mStatusActionMvpImpl.onLikeStatusComplete(topic, taskState);
    }

    @Override
    public void onCollectStatusComplete(Status topic, int taskState) {
        mStatusActionMvpImpl.onCollectStatusComplete(topic, taskState);
    }
}
