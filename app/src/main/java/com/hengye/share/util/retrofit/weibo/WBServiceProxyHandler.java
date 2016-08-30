package com.hengye.share.util.retrofit.weibo;

import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.util.HandlerUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.intercept.AdTokenInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
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
//                                                             Observable<?> x = checkApiError(throwable);
//                                                             if (x != null) return x;
                                                             if (throwable instanceof HttpException) {
                                                                 HttpException httpException = (HttpException) throwable;
                                                                 if (httpException.code() == 403) {
                                                                     HandlerUtil.getInstance().post(new Runnable() {
                                                                         @Override
                                                                         public void run() {
                                                                             ToastUtil.showToast("权限不足");

                                                                             if(BaseActivity.getCurrentActivity() != null) {
                                                                                 new AdTokenInterceptor(BaseActivity.getCurrentActivity()).start();
                                                                             }
                                                                         }
                                                                     });
                                                                 }
                                                             }
                                                             return Observable.error(throwable);
                                                         }
                                                     }
                                             );
                                 }
                             }

                        , Schedulers.trampoline());
    }
}
