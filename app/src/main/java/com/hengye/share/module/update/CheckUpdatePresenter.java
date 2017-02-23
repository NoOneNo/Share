package com.hengye.share.module.update;

import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

/**
 * Created by yuhy on 2016/11/21.
 */

public class CheckUpdatePresenter extends RxPresenter<CheckUpdateContract.View> implements CheckUpdateContract.Presenter {

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

        Single
                .just(0)
                .delay(10000, TimeUnit.MILLISECONDS)
                .flatMap(new Function<Integer, SingleSource<UpdateBombBean>>() {
                    @Override
                    public SingleSource<UpdateBombBean> apply(Integer integer) throws Exception {

                        if(SettingHelper.isLaunchCheckUpdate()) {
                            return RetrofitManager
                                    .getShareService()
                                    .checkUpdate();
                        }
                        return Single.just(new UpdateBombBean());
                    }
                })
                .flatMap(new Function<UpdateBombBean, SingleSource<UpdateBean>>() {
                    @Override
                    public SingleSource<UpdateBean> apply(UpdateBombBean updateBombBean) {
                        UpdateBean updateBean = null;
                        if (updateBombBean != null && !CommonUtil.isEmpty(updateBombBean.getResults())) {
                            updateBean = updateBombBean.getResults().get(0);
                        }

                        if (updateBean != null && updateBean.isNeedUpdate(mIsForce)) {
                            updateBean.setUpdateInfo(filterText(updateBean.getUpdateInfo()));
                            return Single.just(updateBean);
                        } else {
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
                        L.debug("check update do onSuccess ");
                    }

                    @Override
                    public void onError(CheckUpdateContract.View view, Throwable e) {
                        if (mIsForce) {
                            view.onCheckUpdateFail(TaskState.getFailState(e));
                        }
                        L.debug("check update do onError ");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
//                        if(SettingHelper.isUseInternalBrowser()){
//                            L.debug("initX5Environment");
//                            QbSdk.initX5Environment(BaseApplication.getInstance(), null);
//                        }
                    }
                });
    }

    //更新信息增加自动换行
    private String filterText(String text) {
        if (CommonUtil.isEmpty(text)) {
            return text;
        }

        String enter = "\n";
        //换行符转义
        text = text.replace("\\n", "");
        text = text.replace("\n", "");
        text = addSpecifiedStr(text, "；", enter);
        text = addSpecifiedStr(text, ";", enter);
        return text;
    }

    private String addSpecifiedStr(String str, String specifiedStr, String addContent) {
        if (CommonUtil.hasEmpty(str, specifiedStr, addContent)) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        int fromIndex = 0;
        int findIndex;
        while ((findIndex = sb.indexOf(specifiedStr, fromIndex)) != -1) {
            int start = findIndex + specifiedStr.length();
            int end = start + addContent.length();
            sb.insert(start, addContent);
//            sb.append(addContent, start, end);
            fromIndex = end;
        }
        return sb.toString();
    }
}
