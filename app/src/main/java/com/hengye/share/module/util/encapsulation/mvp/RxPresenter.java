package com.hengye.share.module.util.encapsulation.mvp;

import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
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

    protected void unsubscribe() {
        if (mCompositeDisposables != null) {
            mCompositeDisposables.clear();
        }
    }

    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposables == null) {
            mCompositeDisposables = new CompositeDisposable();
        }
        mCompositeDisposables.add(disposable);
    }

    public abstract class BaseSingleObserver<T> extends DefaultSingleObserver<T> implements SingleCallback<V, T>{

        @Override
        public void onSubscribe(Disposable d) {
            addDisposable(d);
        }

        @Override
        public void onSuccess(T t) {
            if (isViewAttached()) {
                onSuccess(getMvpView(), t);
            }
            onComplete();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            if (isViewAttached()) {
                onError(getMvpView(), e);
            }
            onComplete();
        }

        @Override
        public void onComplete(){}

        @Override
        abstract public void onSuccess(V v, T t);

        @Override
        abstract public void onError(V v, Throwable e);
    }

    private abstract class DefaultSingleObserver<T> implements SingleObserver<T>, MaybeObserver<T> {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onSuccess(T t) {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }

    interface SingleCallback<V, T>{
        void onSuccess(V v, T t);

        void onError(V v, Throwable e);
    }
}

