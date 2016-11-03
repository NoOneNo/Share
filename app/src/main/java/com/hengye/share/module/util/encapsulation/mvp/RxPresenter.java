package com.hengye.share.module.util.encapsulation.mvp;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxPresenter<V extends MvpView> extends BasePresenter<V> {


    private CompositeSubscription mSubscriptions;

    public RxPresenter(V mvpView){
        super(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        unsubscribe();
    }

    public void unsubscribe(){
        if(mSubscriptions != null){
            mSubscriptions.clear();
        }
    }


    public void addSubscription(Subscription subscription){
        if(mSubscriptions == null){
            mSubscriptions = new CompositeSubscription();
        }
        mSubscriptions.add(subscription);
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

