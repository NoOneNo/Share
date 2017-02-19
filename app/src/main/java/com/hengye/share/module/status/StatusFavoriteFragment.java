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
    public void onCollectStatusComplete(Status status, int taskState) {
        super.onCollectStatusComplete(status, taskState);
        if(!status.isFavorited()){
            mAdapter.removeItem(status);
        }else{
            if(!mAdapter.contains(status)){
                onScrollToTop(false);
                mAdapter.addItem(0, status);
            }
        }
    }
}
