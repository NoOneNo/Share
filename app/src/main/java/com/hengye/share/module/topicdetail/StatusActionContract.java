package com.hengye.share.module.topicdetail;

import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicComments;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;
import com.hengye.share.module.util.encapsulation.mvp.TaskMvpView;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface StatusActionContract {

    interface View extends MvpView {

        /**
         * 开始点赞微博
         * @param topic
         */
        void onLikeStatusStart(Topic topic);

        /**
         * 结束点赞微博
         * @param topic
         * @param taskState
         */
        void onLikeStatusComplete(Topic topic, int taskState);

        /**
         * 开始收藏微博
         * @param topic
         */
        void onCollectStatusStart(Topic topic);

        /**
         * 结束收藏微博
         * @param topic
         * @param taskState
         */
        void onCollectStatusComplete(Topic topic, int taskState);
    }

    interface Presenter extends MvpPresenter<View> {

        void likeStatus(final Topic topic);

        void collectStatus(final Topic topic);
    }
}
