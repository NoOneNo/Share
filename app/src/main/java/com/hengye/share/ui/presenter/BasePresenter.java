package com.hengye.share.ui.presenter;

import com.hengye.share.ui.mvpview.MvpView;
import com.hengye.share.ui.presenter.Presenter;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<T extends MvpView> implements Presenter<T> {

    private T mMvpView;

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
    }

    public boolean isViewAttached() {
        return mMvpView != null;
    }

    public T getMvpView() {
        return mMvpView;
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

        }

        @Override
        public void onError(Throwable e) {
            if(getMvpView() != null){
                handleViewOnFail(getMvpView(), e);
            }
        }

        @Override
        public void onNext(D d) {
            if(getMvpView() != null){
                handleViewOnSuccess(getMvpView(), d);
            }
        }

        abstract public void handleViewOnSuccess(T v, D d);

        public void handleViewOnFail(T v, Throwable e){
            e.printStackTrace();
        }
    }

    public abstract class BaseAction1<A> implements Action1<A>{
        @Override
        public void call(A a) {
            if(getMvpView() != null){
                handleView(getMvpView(), a);
            }
        }

        abstract public void handleView(T v, A a);

    }
}

