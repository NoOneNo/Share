package com.hengye.share.module.profile;

import com.hengye.share.R;
import com.hengye.share.module.topic.TopicFragment;
import com.hengye.share.module.topic.TopicPresenter;

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
    public int getLayoutResId() {
        return R.layout.fragment_content;
    }


    public int getLoadingResId(){
        return R.layout.widget_loading_top;
    }

    public int getEmptyResId(){
        return R.layout.widget_empty_top;
    }

    public int getNoNetworkResId(){
        return R.layout.widget_no_network_top;
    }
}
