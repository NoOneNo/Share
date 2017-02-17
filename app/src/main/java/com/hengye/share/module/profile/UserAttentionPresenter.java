package com.hengye.share.module.profile;

import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class UserAttentionPresenter extends RxPresenter<UserAttentionContract.View>
        implements UserAttentionContract.Presenter {

    private boolean loading = false;

    public UserAttentionPresenter(UserAttentionContract.View mvpView) {
        super(mvpView);
    }

    @Override
    public void followUser(final UserInfo userInfo) {

        if(loading){
            return;
        }

        Single<WBUserInfo> flowable;

        boolean isFollow = !userInfo.isFollowing();
        String uid = userInfo.getUid();
        flowable = isFollow ?
                RetrofitManager
                        .getWBService()
                        .createFollow(UserUtil.getPriorToken(), uid) :
                RetrofitManager
                        .getWBService()
                        .destroyFollow(UserUtil.getPriorToken(), uid);

        flowable.subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<WBUserInfo>() {
                    @Override
                    public void onSuccess(UserAttentionContract.View view, WBUserInfo wbUserInfo) {
                        loading = false;
                        userInfo.setFollowing(!userInfo.isFollowing());
                        view.onFollowSuccess(userInfo);
                        view.onFollowComplete(TaskState.STATE_SUCCESS);
                    }

                    @Override
                    public void onError(UserAttentionContract.View view, Throwable e) {
                        loading = false;
                        view.onFollowComplete(TaskState.getFailState(e));
                    }

                });
    }
}
