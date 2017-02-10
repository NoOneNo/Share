package com.hengye.share.module.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.module.topic.TopicFragment;
import com.hengye.share.module.topic.TopicPresenter;
import com.hengye.share.util.handler.TopicRefreshIdHandler;

/**
 * Created by yuhy on 2016/10/19.
 */

public class PersonalTopicFragment extends TopicFragment {

    public static PersonalTopicFragment newInstance(TopicPresenter.TopicType topicType, String uid, String name) {
        PersonalTopicFragment fragment = new PersonalTopicFragment();
        fragment.setArguments(getBundle(new TopicPresenter.TopicGroup(topicType), uid, name));
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void setDataHandler() {
        setDataHandler(new TopicRefreshIdHandler<>(getAdapter()));
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_content;
    }

    @Override
    public int getLoadingResId(){
        return R.layout.state_loading_top;
    }

    @Override
    public int getEmptyResId(){
        return R.layout.state_empty_top;
    }

    @Override
    public int getNoNetworkResId(){
        return R.layout.state_no_network_top;
    }

    @Override
    public int getServiceErrorResId() {
        return R.layout.state_service_error_top;
    }
}
