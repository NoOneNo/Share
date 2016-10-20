package com.hengye.share.module.mvp;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<V extends MvpView> implements Presenter<V> {

    private V mMvpView;

    private CompositeSubscription mSubscriptions;

    public BasePresenter(V mvpView){
        attachView(mvpView);
    }

    @Override
    public void attachView(V mvpView) {
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

    public V getMvpView() {
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
                onComplete(getMvpView());
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            if(isViewAttached()){
                onError(getMvpView(), e);
            }
        }

        @Override
        public void onNext(D d) {
            if(isViewAttached()){
                onNext(getMvpView(), d);
            }
        }

        public void onError(V v, Throwable e){}

        public void onComplete(V v){}

        abstract public void onNext(V v, D d);
    }
}

