package com.hengye.share.ui.mvpview;

import com.hengye.share.model.Topic;

import java.util.List;

public interface TopicMvpView extends MvpView {

    void stopLoading(boolean isRefresh);

    void handleNoMoreTopics();

    void handleTopicData(List<Topic> data, boolean isRefresh);

}
