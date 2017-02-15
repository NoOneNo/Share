package com.hengye.share.module.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.util.ResUtil;

import java.util.ArrayList;

/**
 * Created by yuhy on 2017/2/15.
 */

public class StatusFragment extends ShareLoadDataCallbackFragment<Topic> implements StatusActionContract.View {

    protected TopicAdapter mAdapter;
    private StatusActionMvpImpl mStatusActionMvpImpl;
    private StatusActionContract.Presenter mStatusActionPresenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter = new TopicAdapter(getContext(), new ArrayList<Topic>(), getRecyclerView()));
        mStatusActionPresenter = new StatusActonPresenter(this);
        mStatusActionMvpImpl = new StatusActionMvpImpl(this, mStatusActionPresenter);
        mAdapter.setStatusActionPresenter(mStatusActionPresenter);
    }

    @Override
    public void onLikeStatusComplete(Topic topic, int taskState) {
        mStatusActionMvpImpl.onLikeStatusComplete(topic, taskState);
    }

    @Override
    public void onCollectStatusComplete(Topic topic, int taskState) {
        mStatusActionMvpImpl.onCollectStatusComplete(topic, taskState);
    }
}
