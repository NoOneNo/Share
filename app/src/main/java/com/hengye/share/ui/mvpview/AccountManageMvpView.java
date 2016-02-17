package com.hengye.share.ui.mvpview;

import com.hengye.share.model.greenrobot.User;

import java.util.List;

public interface AccountManageMvpView extends MvpView {

    void loadSuccess(List<User> data);

    void loadFail();
}
