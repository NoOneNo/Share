package com.hengye.share.module.statuscomment;

import com.hengye.share.model.StatusComment;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface CommentContract {

    interface View extends ListDataMvpView<StatusComment> {}

    interface Presenter extends MvpPresenter<View> {
        void loadWBComment(String id, final boolean isRefresh);

        void loadWBComment();
    }
}
