package com.hengye.share.module.sso;

import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface UserContract {

    interface View extends MvpView {
        void loadUserInfoFail();

        void loadUserInfoSuccess(UserInfo userInfo);
    }

    interface Presenter extends MvpPresenter<View> {
        void loadWBUserInfo();

        void loadWBUserInfo(String uid, String name);
    }
}
