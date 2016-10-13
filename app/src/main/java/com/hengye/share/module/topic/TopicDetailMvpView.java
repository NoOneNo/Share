package com.hengye.share.module.topic;

import com.hengye.share.model.TopicComment;
import com.hengye.share.module.mvp.MvpView;

import java.util.List;

public interface TopicDetailMvpView extends MvpView {

    void onTaskComplete(boolean isRefresh, boolean isSuccess);

    void handleCommentData(boolean isComment, List<TopicComment> data, boolean isRefresh, long totalNumber);
}
