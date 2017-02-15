package com.hengye.share.module.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.module.topicdetail.StatusActionContract;
import com.hengye.share.module.topicdetail.StatusActonPresenter;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by yuhy on 2017/2/15.
 */

public class StatusFragment extends ShareLoadDataCallbackFragment<Topic> implements StatusActionContract.View {

    protected TopicAdapter mAdapter;
    private StatusActionContract.Presenter mStatusActionPresenter;

    private boolean likeStatusAction = true;
    private boolean collectStatusAction = true;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter = new TopicAdapter(getContext(), new ArrayList<Topic>(), getRecyclerView()));
        mStatusActionPresenter = new StatusActonPresenter(this);
        mAdapter.setStatusActionPresenter(mStatusActionPresenter);
    }

    @Override
    public void onLikeStatusStart(Topic topic) {

    }

    @Override
    public void onLikeStatusComplete(final Topic topic, int taskState) {

        View parent = getParent();
        String tip;
        boolean isSuccess = TaskState.isSuccess(taskState);
        if (isSuccess) {
            tip = ResUtil.getString(
                    topic.isLiked() ?
                            R.string.tip_status_like_create_success :
                            R.string.tip_status_like_destroy_success);
        } else {
            tip = TaskState.toString(taskState);
        }

        Snackbar sb = Snackbar.make(parent, tip, Snackbar.LENGTH_SHORT);
        if(likeStatusAction){
            sb.setAction(isSuccess ?
                    R.string.tip_repeal : R.string.tip_retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeStatusAction = false;
                    mStatusActionPresenter.likeStatus(topic);
                }
            });
        }else{
            likeStatusAction = true;
        }
        sb.show();
    }

    @Override
    public void onCollectStatusStart(Topic topic) {

    }

    @Override
    public void onCollectStatusComplete(final Topic topic, int taskState) {

        View parent = getParent();
        String tip;
        boolean isSuccess = TaskState.isSuccess(taskState);
        if (isSuccess) {
            tip = ResUtil.getString(
                    topic.isFavorited() ?
                            R.string.tip_status_favorite_create_success :
                            R.string.tip_status_favorite_destroy_success);
        } else {
            tip = TaskState.toString(taskState);
        }

        Snackbar sb = Snackbar.make(parent, tip, Snackbar.LENGTH_SHORT);
        if(collectStatusAction){
            sb.setAction(isSuccess ?
                    R.string.tip_repeal : R.string.tip_retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collectStatusAction = false;
                    mStatusActionPresenter.collectStatus(topic);
                }
            });
        }else{
            collectStatusAction = true;
        }
        sb.show();
    }
}
