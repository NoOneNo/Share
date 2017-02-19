package com.hengye.share.module.status;

import com.hengye.share.model.Status;
import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface StatusContract {

    interface View extends ListDataMvpView<Status> {
        void deleteStatusResult(int taskState, Status status);
    }

    interface Presenter extends MvpPresenter<View> {
    }
}
