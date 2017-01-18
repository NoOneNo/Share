package com.hengye.share.module.sso;

import android.text.TextUtils;

import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class UserPresenter extends RxPresenter<UserContract.View> implements UserContract.Presenter{

    public UserPresenter(UserContract.View mvpView){
        super(mvpView);
    }

    @Override
    public void loadWBUserInfo(){
        loadWBUserInfo(UserUtil.getUid(), null);
    }

    @Override
    public void loadWBUserInfo(String uid, String name){
        if(CommonUtil.isEmpty(uid, name)){
            getMvpView().loadSuccess(null);
            return;
        }

        RetrofitManager
                .getWBService()
                .listUserInfo(getWBUserInfoParameter(uid, name))
                .flatMap(new Function<WBUserInfo, SingleSource<WBUserInfo>>() {
                    @Override
                    public SingleSource<WBUserInfo> apply(WBUserInfo wbUserInfo) throws Exception {
                        UserUtil.updateUserInfo(wbUserInfo);
                        return Single.just(wbUserInfo);
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<WBUserInfo>(){
                    @Override
                    public void onSuccess(UserContract.View view, WBUserInfo wbUserInfo) {
                        view.handleUserInfo(wbUserInfo);
                        view.loadSuccess(User.getUser(wbUserInfo));
                    }

                    @Override
                    public void onError(UserContract.View view, Throwable e) {
                        view.loadFail();
                    }
                });
    }

    private Map<String, String> getWBUserInfoParameter(String uid, String name) {
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
