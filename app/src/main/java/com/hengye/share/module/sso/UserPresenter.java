package com.hengye.share.module.sso;

import android.text.TextUtils;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class UserPresenter extends RxPresenter<UserMvpView> {

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
                .filter(new Predicate<WBUserInfo>() {
                    @Override
                    public boolean test(WBUserInfo wbUserInfo) throws Exception {
                        UserUtil.updateUserInfo(wbUserInfo);
                        return true;
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSubscriber<WBUserInfo>() {
                    @Override
                    public void onError(UserMvpView v, Throwable e) {
                        v.loadFail();
                    }

                    @Override
                    public void onNext(UserMvpView v, WBUserInfo wbUserInfo) {
                        v.handleUserInfo(wbUserInfo);
                        v.loadSuccess(User.getUser(wbUserInfo));
                    }

                    @Override
                    public void onNext(WBUserInfo wbUserInfo) {
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
