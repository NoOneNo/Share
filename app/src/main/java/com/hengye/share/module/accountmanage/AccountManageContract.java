package com.hengye.share.module.accountmanage;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

import java.util.List;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface AccountManageContract {

    interface View extends MvpView {
        void loadSuccess(List<User> data, int currentUserIndex);

        void loadFail();
    }

    interface Presenter extends MvpPresenter<View> {
        void loadUsers();
    }
}
