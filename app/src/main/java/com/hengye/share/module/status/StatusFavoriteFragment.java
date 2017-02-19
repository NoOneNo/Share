package com.hengye.share.module.status;

import com.hengye.share.model.Status;

/**
 * Created by yuhy on 2017/2/15.
 */

public class StatusFavoriteFragment extends StatusPageFragment {

    public static StatusFavoriteFragment newInstance() {

        StatusFavoriteFragment fragment = new StatusFavoriteFragment();
        fragment.setArguments(getStartBundle(new StatusPagePresenter.StatusGroup(StatusPagePresenter.StatusType.FAVORITES), null));
        return fragment;
    }

    @Override
    public void onCollectStatusComplete(Status topic, int taskState) {
        super.onCollectStatusComplete(topic, taskState);
        if(!topic.isFavorited()){
            mAdapter.removeItem(topic);
        }else{
            if(!mAdapter.contains(topic)){
                onScrollToTop(false);
                mAdapter.addItem(0, topic);
            }
        }
    }
}
