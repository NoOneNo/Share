package com.hengye.share.util.rxjava;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action1;
import rx.functions.Actions;
import rx.functions.Func1;
import rx.internal.util.InternalObservableUtils;

/**
 * Created by yuhy on 2016/10/4.
 */

public class RxUtil {

    @SafeVarargs
    public static <T> Observable<List<T>> filterEmpty(Observable<List<T>>... array){
        return Observable.concat(Observable.from(array))
                .filter(new Func1<List<T>, Boolean>() {
                    @Override
                    public Boolean call(List<T> data) {
                        return !data.isEmpty();
                    }
                }).first();
    }


    public static <T> Subscription subscribeIgnoreAll(Observable<T> observable){
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
