package com.hengye.share.module.topic;

import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;

public interface TopicMvpView extends ListDataMvpView<Topic> {

    void deleteTopicResult(int taskState, Topic topic);
}
