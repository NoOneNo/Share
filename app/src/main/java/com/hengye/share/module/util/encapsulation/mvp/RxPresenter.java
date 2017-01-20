package com.hengye.share.module.util.encapsulation.mvp;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

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

    public abstract class BaseObserver<T> extends DisposableObserver<T> {

        @Override
        protected void onStart() {
            addDisposable(this);
        }

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
        public void onNext(T t) {
            if (isViewAttached()) {
                onNext(getMvpView(), t);
            }
        }

        public void onError(V v, Throwable e) {
        }

        public void onComplete(V v) {
        }

        abstract public void onNext(V v, T t);
    }

    public abstract class BaseSingleObserver<T> extends DefaultSingleObserver<T> {

        @Override
        public void onSubscribe(Disposable d) {
            addDisposable(d);
        }

        @Override
        public void onSuccess(T t) {
            if (isViewAttached()) {
                onSuccess(getMvpView(), t);
            }
        }

        @Override
        public void onError(Throwable e) {
            if (isViewAttached()) {
                onError(getMvpView(), e);
            }
        }

        abstract public void onSuccess(V v, T t);

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
}

