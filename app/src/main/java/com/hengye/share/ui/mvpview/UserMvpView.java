package com.hengye.share.ui.mvpview;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;

public interface UserMvpView extends MvpView {

    void loadSuccess();

    void loadFail();

    void handleUserInfo(User user);

    void handleUserInfo(WBUserInfo wbUserInfo);
}
