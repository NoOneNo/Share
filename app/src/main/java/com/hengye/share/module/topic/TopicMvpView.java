package com.hengye.share.module.topic;

import com.hengye.share.model.Topic;
import com.hengye.share.module.mvp.ListDataMvpView;
import com.hengye.share.module.mvp.MvpView;
import com.hengye.share.module.util.encapsulation.paging.ListDataCallBack;

import java.util.List;

public interface TopicMvpView extends ListDataMvpView<Topic> {

//    void onTaskComplete(boolean isRefresh, boolean isSuccess);

    void handleCache(List<Topic> data);

//    void handleTopicData(List<Topic> data, boolean isRefresh);

}
