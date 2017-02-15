package com.hengye.share.module.topic;

import com.hengye.share.model.Topic;

/**
 * Created by yuhy on 2017/2/15.
 */

public class TopicFavoriteFragment extends TopicPageFragment {

    public static TopicFavoriteFragment newInstance() {

        TopicFavoriteFragment fragment = new TopicFavoriteFragment();
        fragment.setArguments(getStartBundle(new TopicPagePresenter.TopicGroup(TopicPagePresenter.TopicType.FAVORITES), null));
        return fragment;
    }

    @Override
    public void onCollectStatusComplete(Topic topic, int taskState) {
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
