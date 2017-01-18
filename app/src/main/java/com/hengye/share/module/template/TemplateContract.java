package com.hengye.share.module.template;

import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface TemplateContract {

    interface View extends MvpView {
    }

    interface Presenter extends MvpPresenter<View> {
    }
}
