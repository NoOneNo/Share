package com.hengye.share.util.rxjava;

import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;

/**
 * Created by yuhy on 2016/10/4.
 */

public class RxUtil {

    private final static Observable.Transformer netSchedulersTransformer = new Observable.Transformer<Object, Object>() {
        @Override
        public Observable<Object> call(Observable<Object> observable) {
            return observable
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui());
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Observable.Transformer<T, T> applyNetSchedulers() {
        return (Observable.Transformer<T, T>) netSchedulersTransformer;
    }

    @SafeVarargs
    public static <T> Observable<List<T>> filterEmpty(Observable<List<T>>... array) {
        return Observable.concat(Observable.from(array))
                .filter(new Func1<List<T>, Boolean>() {
                    @Override
                    public Boolean call(List<T> data) {
                        return !data.isEmpty();
                    }
                }).first();
    }


    public static <T> Subscription subscribeIgnoreAll(Observable<T> observable) {
        return observable.subscribe(Actions.empty(), ERROR_NOT_THROW, Actions.empty());
    }

    /**
     * Igonre an Exception when called.
     */
    public static final Action1<Throwable> ERROR_NOT_THROW = new ErrorNotThrowAction();

    private static final class ErrorNotThrowAction implements Action1<Throwable> {
        @Override
        public void call(Throwable t) {
            t.printStackTrace();
        }
    }
}
