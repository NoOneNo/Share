package com.hengye.share.ui.mvpview;

import com.hengye.share.model.TopicComment;

import java.util.List;

public interface TopicDetailMvpView extends MvpView {

    void loadSuccess(boolean isRefresh);

    void loadFail(boolean isRefresh);

    void stopLoading(boolean isRefresh);

    void handleCommentData(boolean isComment, List<TopicComment> data, boolean isRefresh, long totalNumber);
}
