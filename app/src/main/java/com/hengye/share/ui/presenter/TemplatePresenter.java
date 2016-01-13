package com.hengye.share.ui.presenter;

import com.hengye.share.ui.base.BasePresenter;
import com.hengye.share.ui.mvpview.TemplateMvpView;

public class TemplatePresenter extends BasePresenter<TemplateMvpView> {

    public TemplatePresenter(TemplateMvpView mvpView){
        super(mvpView);
    }

    @Override
    public void attachView(TemplateMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}