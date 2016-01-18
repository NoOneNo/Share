package com.hengye.share.ui.mvpview;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.ui.base.MvpView;

public interface UserMvpView extends MvpView {

    void handleUserInfo(User user);
}
