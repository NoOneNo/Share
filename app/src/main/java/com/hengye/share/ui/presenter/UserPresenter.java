package com.hengye.share.ui.presenter;

import android.text.TextUtils;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.mvpview.UserMvpView;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserPresenter extends BasePresenter<UserMvpView> {

    public UserPresenter(UserMvpView mvpView){
        super(mvpView);
    }

    public void loadWBUserInfo(){
        loadWBUserInfo(UserUtil.getUid(), null);
    }

    public void loadWBUserInfo(String uid, String name){
        RetrofitManager
                .getWBService()
                .listUserInfo(getWBUserInfoParameter(uid, name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<WBUserInfo>() {
                    @Override
                    public void handleViewOnFail(UserMvpView v, Throwable e) {

                    }

                    @Override
                    public void handleViewOnSuccess(UserMvpView v, WBUserInfo wbUserInfo) {
                        v.handleUserInfo(wbUserInfo);
                        v.handleUserInfo(User.getUser(wbUserInfo));
                    }


                    @Override
                    public void onNext(WBUserInfo wbUserInfo) {
                        UserUtil.updateUserInfo(wbUserInfo);
                        super.onNext(wbUserInfo);
                    }
                });
    }

    public Map<String, String> getWBUserInfoParameter(String uid, String name) {
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getToken());
        if (!TextUtils.isEmpty(uid)) {
            ub.addParameter("uid", uid);
        } else if (!TextUtils.isEmpty(name)) {
            ub.addParameter("screen_name", name);
        }
        return ub.getParameters();
    }
}
