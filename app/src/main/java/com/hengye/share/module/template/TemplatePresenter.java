package com.hengye.share.module.template;

import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;

public class TemplatePresenter extends RxPresenter<TemplateContract.View>
        implements TemplateContract.Presenter {

    public TemplatePresenter(TemplateContract.View mvpView) {
        super(mvpView);
    }

}
