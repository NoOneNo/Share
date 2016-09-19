package com.hengye.share.module.accountmanage;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.module.mvp.MvpView;

import java.util.List;

public interface AccountManageMvpView extends MvpView {

    void loadSuccess(List<User> data, int currentUserIndex);

    void loadFail();
}
