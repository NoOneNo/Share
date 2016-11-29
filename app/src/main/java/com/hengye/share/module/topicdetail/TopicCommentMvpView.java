package com.hengye.share.module.topicdetail;

import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicComments;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

public interface TopicCommentMvpView extends ListDataMvpView<TopicComment> {

    void onLoadTopicComments(TopicComments topicComments);

    void onTopicCommentLike(TopicComment topicComment, int taskState);

}
