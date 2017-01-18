package com.hengye.share.module.update;

import android.support.annotation.NonNull;

import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface CheckUpdateContract {

    interface View extends MvpView {
        void onCheckUpdateStart();

        void onCheckUpdateComplete(@NonNull UpdateBean updateBean);

        void onCheckUpdateFail(int taskState);
    }

    interface Presenter extends MvpPresenter<View> {
        void checkUpdate();
    }
}
