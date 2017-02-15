package com.hengye.share.module.topic;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.fragment.BaseFragment;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;
import com.hengye.share.util.ResUtil;

/**
 * Created by yuhy on 2017/2/16.
 */

public class StatusActionMvpImpl implements StatusActionContract.View {

    private StatusActionContract.Presenter mStatusActionPresenter;
    private MvpView mvpView;
    private View parent;

    private boolean likeStatusAction = true;
    private boolean collectStatusAction = true;

    public StatusActionMvpImpl(BaseActivity baseActivity, StatusActionContract.Presenter presenter) {
        this(baseActivity,
                baseActivity.findViewById(android.R.id.content),
                presenter);
    }

    public StatusActionMvpImpl(BaseFragment baseFragment, StatusActionContract.Presenter presenter) {
        this(baseFragment, baseFragment.getParent(), presenter);
    }

    public StatusActionMvpImpl(MvpView mvpView, View parent, StatusActionContract.Presenter presenter) {
        this.mvpView = mvpView;
        this.parent = parent;
        mStatusActionPresenter = presenter;
    }

    @Override
    public void onLikeStatusComplete(final Topic topic, int taskState) {

        View parent = this.parent;
        if(parent == null){
            return;
        }
        String tip;
        final boolean isSuccess = TaskState.isSuccess(taskState);
        if (isSuccess) {
            tip = ResUtil.getString(
                    topic.isLiked() ?
                            R.string.tip_status_like_create_success :
                            R.string.tip_status_like_destroy_success);
        } else {
            tip = TaskState.toString(taskState);
        }

        Snackbar sb = Snackbar.make(parent, tip, isSuccess ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_LONG);
        if (likeStatusAction) {
            sb.setAction(isSuccess ?
                    R.string.tip_repeal : R.string.tip_retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSuccess) {
                        likeStatusAction = false;
                    }
                    mStatusActionPresenter.likeStatus(topic);
                }
            });
        } else {
            likeStatusAction = true;
        }
        sb.show();
    }

    @Override
    public void onCollectStatusComplete(final Topic topic, int taskState) {

        View parent = this.parent;
        if(parent == null){
            return;
        }
        String tip;
        final boolean isSuccess = TaskState.isSuccess(taskState);
        if (isSuccess) {
            tip = ResUtil.getString(
                    topic.isFavorited() ?
                            R.string.tip_status_favorite_create_success :
                            R.string.tip_status_favorite_destroy_success);
        } else {
            tip = TaskState.toString(taskState);
        }

        Snackbar sb = Snackbar.make(parent, tip, isSuccess ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_LONG);
        if(collectStatusAction){
            sb.setAction(isSuccess ?
                    R.string.tip_repeal : R.string.tip_retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isSuccess) {
                        collectStatusAction = false;
                    }
                    mStatusActionPresenter.collectStatus(topic);
                }
            });
        }else{
            collectStatusAction = true;
        }
        sb.show();
    }

    @Override
    public void setPresenter(MvpPresenter presenter) {
        mvpView.setPresenter(presenter);
    }
}
