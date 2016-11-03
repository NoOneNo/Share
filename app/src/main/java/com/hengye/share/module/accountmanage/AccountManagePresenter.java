package com.hengye.share.module.accountmanage;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UserUtil;

import java.util.List;

public class AccountManagePresenter extends RxPresenter<AccountManageMvpView> {

    public AccountManagePresenter(AccountManageMvpView mvpView){
        super(mvpView);
    }

    public void loadUsers(){
        List<User> users = UserUtil.queryUsers();
        if(!CommonUtil.isEmpty(users)){
            User user = UserUtil.getCurrentUser();
            getMvpView().loadSuccess(users, users.indexOf(user));
        }else{
            getMvpView().loadFail();
        }
    }
}
