package com.hengye.share.util.rxjava.datasource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by yuhy on 2016/11/23.
 */

public class ObservableHelper {

    public static <T> Observable<ArrayList<T>> justArrayList(List<T> t){
        ArrayList<T> arrayList = new ArrayList<>();
        if(t != null){
            arrayList.addAll(t);
        }
        return Observable.just(arrayList);
    }


    public static <T> Observable<ArrayList<T>> justArrayList(ArrayList<T> t){
        return t == null ? Observable.just(new ArrayList<T>()) : Observable.just(t);
    }

    public static <T> Observable<T> just(T t){
        return t == null ? Observable.<T>empty() : Observable.just(t);
    }
}
