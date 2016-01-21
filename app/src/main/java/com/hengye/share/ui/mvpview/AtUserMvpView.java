package com.hengye.share.ui.mvpview;

import com.hengye.share.model.UserInfo;

import java.util.List;

public interface AtUserMvpView extends MvpView {

    void showSuccess(List<UserInfo> data);

    void showFail();
}
