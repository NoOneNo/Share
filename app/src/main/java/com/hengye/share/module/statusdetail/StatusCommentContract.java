package com.hengye.share.module.statusdetail;

import com.hengye.share.model.StatusComment;
import com.hengye.share.model.StatusComments;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface StatusCommentContract {

    interface View extends ListDataMvpView<StatusComment> {
        void onLoadStatusComments(StatusComments statusComments);

        void onStatusCommentLike(StatusComment statusComment, int taskState);
    }

    interface Presenter extends MvpPresenter<View> {

        void loadWBCommentOrRepost(String topicId, String id, final boolean isRefresh, final boolean isComment);

        void loadWBHotComment(boolean isRefresh, String topicId, int page, int count);

        void likeComment(final StatusComment statusComment);
    }
}
