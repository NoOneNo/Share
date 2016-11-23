package com.hengye.share.module.util.encapsulation.mvp;

import com.hengye.share.util.rxjava.DefaultSubscriber;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxPresenter<V extends MvpView> extends BasePresenter<V> {

    private CompositeDisposable mCompositeDisposables;

    public RxPresenter(V mvpView) {
        super(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        unsubscribe();
    }

    public void unsubscribe() {
        if(mCompositeDisposables != null){
            mCompositeDisposables.clear();
        }
    }

    public void addDisposable(Disposable disposable){
        if(mCompositeDisposables == null){
            mCompositeDisposables = new CompositeDisposable();
        }
        mCompositeDisposables.add(disposable);
    }


    public abstract class BaseSubscriber<D> extends DefaultSubscriber<D> {

        @Override
        public void onComplete() {
            if (isViewAttached()) {
                onComplete(getMvpView());
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            if (isViewAttached()) {
                onError(getMvpView(), e);
            }
        }

        @Override
        public void onNext(D d) {
            if (isViewAttached()) {
                onNext(getMvpView(), d);
            }
        }

        public void onError(V v, Throwable e) {
        }

        public void onComplete(V v) {
        }

        abstract public void onNext(V v, D d);
    }
}

