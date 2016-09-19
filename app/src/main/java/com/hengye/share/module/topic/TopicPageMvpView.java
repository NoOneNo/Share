package com.hengye.share.module.topic;

import com.hengye.share.model.Topic;
import com.hengye.share.module.mvp.MvpView;

import java.util.List;

public interface TopicPageMvpView extends MvpView {

    void stopLoading(boolean isRefresh);

    void handleTopicData(List<Topic> data, boolean isRefresh);

}
