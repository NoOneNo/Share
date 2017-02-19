package com.hengye.share.module.statusdetail;

import com.hengye.share.model.StatusComment;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

import java.util.List;

public interface StatusDetailMvpView extends MvpView {

    void onTaskComplete(boolean isRefresh, boolean isSuccess);

    void handleCommentData(boolean isComment, List<StatusComment> data, boolean isRefresh, long totalNumber);
}
