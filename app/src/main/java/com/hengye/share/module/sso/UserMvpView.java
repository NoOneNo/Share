package com.hengye.share.module.sso;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.module.mvp.MvpView;

public interface UserMvpView extends MvpView {

    void loadSuccess(User user);

    void loadFail();

    void handleUserInfo(WBUserInfo wbUserInfo);
}
