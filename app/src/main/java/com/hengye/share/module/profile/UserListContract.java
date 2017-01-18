package com.hengye.share.module.profile;

import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

import java.util.List;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface UserListContract {

    interface View extends ListDataMvpView<UserInfo> {

//        void showUserListSuccess(List<UserInfo> data, boolean isRefresh);
//
//        void onTaskComplete(boolean isRefresh, boolean isSuccess);
//
//        void canLoadMore(boolean loadEnable);
    }

    interface Presenter extends MvpPresenter<View> {
    }
}
