package com.hengye.share.module.topicdetail;

import com.hengye.share.model.TopicComment;
import com.hengye.share.module.mvp.ListDataMvpView;
import com.hengye.share.module.mvp.MvpView;

import java.util.List;

public interface TopicCommentMvpView extends ListDataMvpView<TopicComment> {

    void updateTotalCount(long totalCount);
}
