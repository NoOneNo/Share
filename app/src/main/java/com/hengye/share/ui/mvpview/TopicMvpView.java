package com.hengye.share.ui.mvpview;

import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.ui.base.MvpView;

import java.util.List;

public interface TopicMvpView extends MvpView {

    void stopLoading(boolean isRefresh);

    void handleNoMoreTopics();

    void handleTopicData(List<Topic> data, boolean isRefresh);

    void handleUserInfo(User user);

}
