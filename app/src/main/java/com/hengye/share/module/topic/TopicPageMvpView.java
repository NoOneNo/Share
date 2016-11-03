package com.hengye.share.module.topic;

import com.hengye.share.model.Topic;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

import java.util.List;

public interface TopicPageMvpView extends MvpView {

    void onTaskComplete(boolean isRefresh, boolean isSuccess);

    void handleTopicData(List<Topic> data, boolean isRefresh);

}
