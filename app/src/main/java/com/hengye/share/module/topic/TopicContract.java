package com.hengye.share.module.topic;

import com.hengye.share.model.Topic;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface TopicContract {

    interface View extends ListDataMvpView<Topic> {
        void deleteTopicResult(int taskState, Topic topic);
    }

    interface Presenter extends MvpPresenter<View> {
    }
}
