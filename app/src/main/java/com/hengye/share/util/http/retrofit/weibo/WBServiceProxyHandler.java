package com.hengye.share.util.http.retrofit.weibo;

import com.hengye.share.util.rxjava.datasource.ObservableHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by yuhy on 16/8/2.
 */
public class WBServiceProxyHandler implements InvocationHandler {

    private Object mObject;

    public WBServiceProxyHandler(Object obj) {
        this.mObject = obj;
    }

    public Function<Observable<? extends Throwable>, Observable<?>> mCheckException = new Function<Observable<? extends Throwable>, Observable<?>>() {
        @Override
        public Observable<?> apply(Observable<? extends Throwable> observable) {
            return observable.
                    flatMap(new Function<Throwable, Observable<?>>() {
                                @Override
                                public Observable<?> apply(Throwable throwable) {
                                    WBServiceErrorHandler.getInstance().checkError(throwable);

                                    return Observable.error(throwable);
                                }
                            }
                    );
        }
    };

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
//        return method.invoke(mObject, args);

        if (method.getReturnType() != Observable.class) {
            return method.invoke(mObject, args);
        }

        try {
            return ((Observable<?>) method.invoke(mObject, args))
                    .retryWhen(mCheckException);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return ObservableHelper.just(new Exception("method call error"));

//        return Observable.fromCallable(new Callable<Observable<?>>() {
//            @Override
//            public Observable<?> call() throws Exception {
//                try {
//                    return (Observable<?>) method.invoke(mObject, args);
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//                return ObservableHelper.just(new Exception("method call error"));
//            }
//        });
//                .retryWhen(new Function<Observable<? extends Throwable>, Observable<?>>() {
//            @Override
//            public Observable<?> apply(Observable<? extends Throwable> observable) {
//                return observable.
//                        flatMap(new Function<Throwable, Observable<?>>() {
//                                    @Override
//                                    public Observable<?> apply(Throwable throwable) {
//                                        WBServiceErrorHandler.getInstance().checkError(throwable);
//
//                                        return Observable.error(throwable);
//                                    }
//                                }
//                        );
//            }
//        });

//        return ObservableHelper.just(null)
//                .flatMap(new Function<Object, Observable<?>>() {
//                    @Override
//                    public Observable<?> apply(Object o) {
//                        try {
//                            return (Observable<?>) method.invoke(mObject, args);
//                        } catch (IllegalAccessException | InvocationTargetException e) {
//                            e.printStackTrace();
//                        }
//                        return ObservableHelper.just(new Exception("method call error"));
//                    }
//                }).retryWhen(new Function<Observable<? extends Throwable>, Observable<?>>() {
//                    @Override
//                    public Observable<?> apply(Observable<? extends Throwable> observable) {
//                        return observable.
//                                flatMap(new Function<Throwable, Observable<?>>() {
//                                            @Override
//                                            public Observable<?> apply(Throwable throwable) {
//                                                WBServiceErrorHandler.getInstance().checkError(throwable);
//
//                                                return Observable.error(throwable);
//                                            }
//                                        }
//                                );
//                    }
//                });
    }
}
