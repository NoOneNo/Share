package com.hengye.share.util.rxjava;

import com.hengye.share.util.rxjava.datasource.ObservableHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.functions.Functions;

/**
 * Created by yuhy on 2016/10/4.
 */

public class RxUtil {

//    public static <T> ObservableTransformer<Optional<T>, T> deoptionalize() {
//        return new ObservableTransformer<Optional<T>, T>() {
//            @Override
//            public ObservableSource<T> apply(Observable<Optional<T>> upstream) {
//                return upstream.flatMap(new Function<Optional<T>, ObservableSource<T>>() {
//                    @Override
//                    public ObservableSource<T> apply(Optional<T> t) throws Exception {
//                        return t.isPresent() ? ObservableHelper.just(t.get()) : (Observable<T>)Observable.empty();
//                    }
//                });
//            }
//        };
//    }

    public static final ObservableTransformer netSchedulersTransformer = new ObservableTransformer() {
        @Override public Observable apply(Observable upstream) {
            return upstream.subscribeOn(SchedulerProvider.io())
                    .unsubscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui());
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> ObservableTransformer<T, T> applyNetSchedulers() {
        return (ObservableTransformer<T, T>) netSchedulersTransformer;
    }

    @SafeVarargs
    public static <T> Single<ArrayList<T>> filterEmpty(Single<ArrayList<T>>... array) {
        return Single.concatArray(array)
                .filter(new Predicate<List<T>>() {
                    @Override
                    public boolean test(List<T> data) {
                        return !data.isEmpty();
                    }
                }).first(new ArrayList<T>());
    }


    public static <T> Disposable subscribeIgnoreAll(Observable<T> observable) {
        return observable.subscribe(Functions.emptyConsumer(), ERROR_NOT_THROW,
                Functions.EMPTY_ACTION);
    }

    /**
     * Igonre an Exception when called.
     */
    public static final Consumer<Throwable> ERROR_NOT_THROW = new ErrorNotThrowAction();

//    public static Observer EMPTY_SUBSCRIBER = new ActionSubscriber(Actions.empty(), ERROR_NOT_THROW, Actions.empty());

    private static final class ErrorNotThrowAction implements Consumer<Throwable> {
        @Override
        public void accept(Throwable t) {
            t.printStackTrace();
        }
    }
}
