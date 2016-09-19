package com.hengye.share.module.profile;

import com.hengye.share.model.UserInfo;
import com.hengye.share.module.mvp.MvpView;

import java.util.List;

public interface UserListMvpView extends MvpView {

    void showUserListSuccess(List<UserInfo> data, boolean isRefresh);

    void loadFail(boolean isRefresh);

    void stopLoading(boolean isRefresh);

    void canLoadMore(boolean loadEnable);
}
