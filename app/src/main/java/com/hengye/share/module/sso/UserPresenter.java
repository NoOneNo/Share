package com.hengye.share.module.sso;

import android.text.TextUtils;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.module.mvp.BasePresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;

import java.util.Map;

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
        if(CommonUtil.isEmpty(uid, name)){
            return;
        }

        RetrofitManager
                .getWBService()
                .listUserInfo(getWBUserInfoParameter(uid, name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<WBUserInfo>() {
                    @Override
                    public void handleViewOnFail(UserMvpView v, Throwable e) {
                        v.loadFail();
                    }

                    @Override
                    public void handleViewOnSuccess(UserMvpView v, WBUserInfo wbUserInfo) {
                        v.handleUserInfo(wbUserInfo);
                        v.loadSuccess(User.getUser(wbUserInfo));
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
        String token = UserUtil.getPriorToken();
        ub.addParameter("access_token", token);
        if (!TextUtils.isEmpty(uid)) {
            ub.addParameter("uid", uid);
        } else if (!TextUtils.isEmpty(name)) {
            ub.addParameter("screen_name", name);
        }
        return ub.getParameters();
    }
}
