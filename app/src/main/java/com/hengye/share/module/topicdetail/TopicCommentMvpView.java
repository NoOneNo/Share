package com.hengye.share.module.topicdetail;

import com.hengye.share.model.TopicComment;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

public interface TopicCommentMvpView extends ListDataMvpView<TopicComment> {

    void updateTotalCount(long totalCount);
}
