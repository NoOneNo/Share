package com.hengye.share.module.status;

import com.hengye.share.model.Status;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface StatusActionContract {

    interface View extends MvpView {

        /**
         * 结束点赞微博
         * @param topic
         * @param taskState
         */
        void onLikeStatusComplete(Status topic, int taskState);

        /**
         * 结束收藏微博
         * @param topic
         * @param taskState
         */
        void onCollectStatusComplete(Status topic, int taskState);
    }

    interface Presenter extends MvpPresenter<View> {

        void likeStatus(final Status topic);

        void collectStatus(final Status topic);
    }
}
