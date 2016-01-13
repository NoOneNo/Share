package com.hengye.share.ui.mvpview;

import com.hengye.share.model.UserInfo;
import com.hengye.share.ui.base.MvpView;

import java.util.List;

public interface AtUserMvpView extends MvpView {

    void showSuccess(List<UserInfo> data);

    void showFail();
}
