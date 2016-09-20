package com.hengye.share.module.mvp;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<T extends MvpView> implements Presenter<T> {

    private T mMvpView;

    private CompositeSubscription mSubscriptions;

    public BasePresenter(T mvpView){
        attachView(mvpView);
    }

    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if(mSubscriptions != null){
            mSubscriptions.clear();
        }
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
    }

    public void addSubscription(Subscription subscription){
        if(mSubscriptions == null){
            mSubscriptions = new CompositeSubscription();
        }
        mSubscriptions.add(subscription);
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }

    public abstract class BaseSubscriber<D> extends Subscriber<D> {
        @Override
        public void onCompleted() {
            if(isViewAttached()){
                onFinish(getMvpView(), false);
            }
        }

        @Override
        public void onError(Throwable e) {
            if(isViewAttached()){
                onError(getMvpView(), e);
                onFinish(getMvpView(), true);
            }
        }

        @Override
        public void onNext(D d) {
            if(isViewAttached()){
                onNext(getMvpView(), d);
            }
        }

        public void onError(T v, Throwable e){
            e.printStackTrace();
        }

        /**
         * This method will be invoked after onCompleted or OnError.
         */
        public void onFinish(T v, boolean hasError){

        }

        abstract public void onNext(T v, D d);
    }
}

