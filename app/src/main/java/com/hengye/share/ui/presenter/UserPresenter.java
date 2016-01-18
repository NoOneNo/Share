package com.hengye.share.ui.presenter;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.base.BasePresenter;
import com.hengye.share.ui.mvpview.TemplateMvpView;
import com.hengye.share.ui.mvpview.UserMvpView;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserPresenter extends BasePresenter<UserMvpView> {

    public UserPresenter(UserMvpView mvpView){
        super(mvpView);
    }

    public void loadWBUserInfo(){
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBUserInfoUrl());
        ub.addParameter("access_token", UserUtil.getToken());
        ub.addParameter("uid", UserUtil.getUid());

        RetrofitManager
                .getWBService()
                .listUserInfo(ub.getParameters())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WBUserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WBUserInfo wbUserInfo) {
                        UserUtil.updateUserInfo(wbUserInfo);
                        getMvpView().handleUserInfo(User.getUser(wbUserInfo));
                    }
                });
    }
}
