package com.hengye.share.module.profile;

import com.hengye.share.model.UserInfo;
import com.hengye.share.module.mvp.MvpView;

import java.util.List;

public interface UserListMvpView extends MvpView {

    void showUserListSuccess(List<UserInfo> data, boolean isRefresh);

    void onTaskComplete(boolean isRefresh, boolean isSuccess);

    void canLoadMore(boolean loadEnable);
}
