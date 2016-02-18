package com.hengye.share.ui.mvpview;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;

public interface UserMvpView extends MvpView {

    void loadSuccess(User user);

    void loadFail();

    void handleUserInfo(WBUserInfo wbUserInfo);
}
