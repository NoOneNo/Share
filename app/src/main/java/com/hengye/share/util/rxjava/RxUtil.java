package com.hengye.share.util.rxjava;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

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
}
