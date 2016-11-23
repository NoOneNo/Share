package com.hengye.share.module.update;

import android.support.annotation.NonNull;

import com.hengye.share.module.util.encapsulation.mvp.MvpView;

/**
 * Created by yuhy on 2016/11/21.
 */

public interface CheckUpdateMvpView extends MvpView {

    void onCheckUpdateStart();

    void onCheckUpdateComplete(@NonNull UpdateBean updateBean);

    void onCheckUpdateFail(int taskState);
}
