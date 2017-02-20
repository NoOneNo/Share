package com.hengye.share.module.profile;

import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface UserAttentionContract {

    interface View extends MvpView{

        void onFollowStart();

        void onFollowComplete(int taskState);

        void onFollowSuccess(UserInfo userInfo);
    }

    interface Presenter extends MvpPresenter<View> {

        void followUser(UserInfo userInfo);
    }
}
