package com.hengye.share.module.userguide;

import com.hengye.share.module.util.encapsulation.mvp.ListDataMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface UserGuideContract {

    interface View extends ListDataMvpView<UserGuide> {}

    interface Presenter extends MvpPresenter<View> {
        void listUserGuide();
    }
}
