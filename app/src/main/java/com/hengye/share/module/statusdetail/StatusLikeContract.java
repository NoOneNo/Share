package com.hengye.share.module.statusdetail;

import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface StatusLikeContract {

    interface View extends ListDataMvpView<UserInfo> {
        void onLoadStatusLikeCount(long count);
    }

    interface Presenter extends MvpPresenter<View> {
        void listStatusLike(boolean isRefresh, String statusId, int page, int count);
    }
}
