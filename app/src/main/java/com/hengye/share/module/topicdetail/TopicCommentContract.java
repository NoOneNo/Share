package com.hengye.share.module.topicdetail;

import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicComments;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface TopicCommentContract {

    interface View extends ListDataMvpView<TopicComment> {
        void onLoadTopicComments(TopicComments topicComments);

        void onTopicCommentLike(TopicComment topicComment, int taskState);
    }

    interface Presenter extends MvpPresenter<View> {

        void loadWBCommentOrRepost(String topicId, String id, final boolean isRefresh, final boolean isComment);

        void loadWBHotComment(boolean isRefresh, String topicId, int page, int count);

        void likeComment(final TopicComment topicComment);
    }
}
