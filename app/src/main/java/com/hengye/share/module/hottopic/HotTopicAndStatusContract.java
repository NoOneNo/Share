package com.hengye.share.module.hottopic;

import com.hengye.share.model.HotTopic;
import com.hengye.share.model.Topic;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface HotTopicAndStatusContract {

    interface View extends ListDataMvpView<Topic> {

        void onLoadHotTopic(ArrayList<HotTopic> hotTopics);
    }

    interface Presenter extends MvpPresenter<View> {

        void loadWBTopic(int id, final boolean isRefresh);
    }


}
