package com.hengye.share.module.update;

import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by yuhy on 2016/11/21.
 */

public class CheckUpdatePresenter extends RxPresenter<CheckUpdateMvpView> {

    public CheckUpdatePresenter(CheckUpdateMvpView mvpView, boolean isForce) {
        super(mvpView);
        this.mIsForce = isForce;
    }

    /**
     * @param mIsForce 如果为true，则无视版本的不再提示，当用户主动检查更新时应该为true
     */
    boolean mIsForce;

    public void checkUpdate() {
        getMvpView().onCheckUpdateStart();
        RetrofitManager
                .getShareService()
                .checkUpdate()
                .delay(5000, TimeUnit.MILLISECONDS)
                .flatMap(new Function<UpdateBombBean, Observable<UpdateBean>>() {
                    @Override
                    public Observable<UpdateBean> apply(UpdateBombBean updateBombBean) {
                        UpdateBean updateBean = null;
                        if (updateBombBean != null && !CommonUtil.isEmpty(updateBombBean.getResults())) {
                            updateBean = updateBombBean.getResults().get(0);
                        }
                        return ObservableHelper.just(updateBean);
                    }
                })
                .filter(new Predicate<UpdateBean>() {
                    @Override
                    public boolean test(UpdateBean updateBean) {
                        return updateBean != null && updateBean.isNeedUpdate(mIsForce);
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSubscriber<UpdateBean>() {
                    @Override
                    public void onNext(CheckUpdateMvpView checkUpdateMvpView, UpdateBean updateBean) {
                        if (updateBean != null) {
                            checkUpdateMvpView.onCheckUpdateComplete(updateBean);
                        }
                    }

                    @Override
                    public void onError(CheckUpdateMvpView checkUpdateMvpView, Throwable e) {
                        if(mIsForce) {
                            checkUpdateMvpView.onCheckUpdateFail(TaskState.getFailState(e));
                        }
                    }
                });
    }
}
