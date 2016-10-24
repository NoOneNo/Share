package com.hengye.share.util.retrofit.weibo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yuhy on 16/8/2.
 */
public class WBServiceProxyHandler implements InvocationHandler {

    private Object mObject;

    public WBServiceProxyHandler(Object obj) {
        this.mObject = obj;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return Observable.just(null)
                .flatMap(new Func1<Object, Observable<?>>() {
                    @Override
                    public Observable<?> call(Object o) {
                        try {
//                            checkTokenValid(method, args);
                            return (Observable<?>) method.invoke(mObject, args);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return Observable.just(new Exception("method call error"));
                    }
                }).retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                                 @Override
                                 public Observable<?> call(Observable<? extends Throwable> observable) {
                                     return observable.
                                             flatMap(new Func1<Throwable, Observable<?>>() {
                                                         @Override
                                                         public Observable<?> call(Throwable throwable) {
                                                             WBServiceErrorHandler.getInstance().checkError(throwable);

                                                             return Observable.error(throwable);
                                                         }
                                                     }
                                             );
                                 }
                             });
    }
}
