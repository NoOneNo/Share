package com.hengye.share.ui.mvpview;

import com.hengye.share.model.UserInfo;

import java.util.List;

public interface UserListMvpView extends MvpView {

    void showUserListSuccess(List<UserInfo> data, boolean isRefresh);

    void loadFail(boolean isRefresh);

    void stopLoading(boolean isRefresh);

    void canLoadMore(boolean loadEnable);
}
