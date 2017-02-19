package com.hengye.share.module.search;

import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface SearchUserContract {

    interface View extends ListDataMvpView<UserInfo> {
    }

    interface Presenter extends MvpPresenter<View> {
        void searchWBUser(String content, boolean isRefresh, int page, int count);
    }
}
