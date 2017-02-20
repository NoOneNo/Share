package com.hengye.share.module.update;

import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

/**
 * Created by yuhy on 2016/11/21.
 */

public class CheckUpdatePresenter extends RxPresenter<CheckUpdateContract.View> implements CheckUpdateContract.Presenter{

    public CheckUpdatePresenter(CheckUpdateContract.View mvpView, boolean isForce) {
        super(mvpView);
        this.mIsForce = isForce;
    }

    /**
     * @param mIsForce 如果为true，则无视版本的不再提示，当用户主动检查更新时应该为true
     */
    boolean mIsForce;

    @Override
    public void checkUpdate() {
        getMvpView().onCheckUpdateStart();
        RetrofitManager
                .getShareService()
                .checkUpdate()
                .delay(5000, TimeUnit.MILLISECONDS)
                .flatMap(new Function<UpdateBombBean, SingleSource<UpdateBean>>() {
                    @Override
                    public SingleSource<UpdateBean> apply(UpdateBombBean updateBombBean) {
                        UpdateBean updateBean = null;
                        if (updateBombBean != null && !CommonUtil.isEmpty(updateBombBean.getResults())) {
                            updateBean = updateBombBean.getResults().get(0);
                        }

                        if(updateBean != null && updateBean.isNeedUpdate(mIsForce)){
                            return Single.just(updateBean);
                        }else{
                            return Single.just(new UpdateBean());
                        }
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<UpdateBean>() {
                    @Override
                    public void onSuccess(CheckUpdateContract.View checkUpdateMvpView, UpdateBean updateBean) {
                        if (updateBean != null) {
                            checkUpdateMvpView.onCheckUpdateComplete(updateBean);
                        }
                    }

                    @Override
                    public void onError(CheckUpdateContract.View checkUpdateMvpView, Throwable e) {
                        if(mIsForce) {
                            checkUpdateMvpView.onCheckUpdateFail(TaskState.getFailState(e));
                        }
                    }
                });
    }
}
