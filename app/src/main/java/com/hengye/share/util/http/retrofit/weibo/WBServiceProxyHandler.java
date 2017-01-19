package com.hengye.share.util.http.retrofit.weibo;

import org.reactivestreams.Publisher;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created by yuhy on 16/8/2.
 */
public class WBServiceProxyHandler implements InvocationHandler {

    private Object mObject;

    public WBServiceProxyHandler(Object obj) {
        this.mObject = obj;
    }

    private Function<Observable<? extends Throwable>, Observable<?>> mRetryObservableException = new Function<Observable<? extends Throwable>, Observable<?>>() {
        @Override
        public Observable<?> apply(Observable<? extends Throwable> observable) {
            return observable.
                    flatMap(new Function<Throwable, Observable<?>>() {
                                @Override
                                public Observable<?> apply(Throwable throwable) {
                                    WBApiException wbApiException = WBServiceErrorHandler.getInstance().checkError(throwable);

                                    return Observable.error(wbApiException != null ? wbApiException : throwable);
                                }
                            }
                    );
        }
    };

    private Function<Flowable<Throwable>, Publisher<Object>> mRetrySingleException = new Function<Flowable<Throwable>, Publisher<Object>>() {
        @Override
        public Publisher<Object> apply(Flowable<Throwable> throwableFlowable) throws Exception {
            return throwableFlowable
                    .flatMap(new Function<Throwable, Publisher<?>>() {
                        @Override
                        public Publisher<?> apply(Throwable throwable) throws Exception {
                            WBApiException wbApiException = WBServiceErrorHandler.getInstance().checkError(throwable);
                            return Flowable.error(wbApiException != null ? wbApiException : throwable);
                        }
                    });
        }
    };

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
//        return method.invoke(mObject, args);

        Class<?> returnType = method.getReturnType();

        try {
            if(returnType == Observable.class) {
                return ((Observable<?>) method.invoke(mObject, args))
                        .retryWhen(mRetryObservableException);
            }else if(returnType == Single.class){
                return ((Single<?>) method.invoke(mObject, args))
                        .retryWhen(mRetrySingleException);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return method.invoke(mObject, args);
    }
}
