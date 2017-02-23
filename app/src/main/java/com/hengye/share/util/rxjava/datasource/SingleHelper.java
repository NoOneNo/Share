package com.hengye.share.util.rxjava.datasource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by yuhy on 2016/11/23.
 */

public class SingleHelper {

    public static <T> Single<ArrayList<T>> justArrayList(List<T> t){
        ArrayList<T> arrayList = new ArrayList<>();
        if(t != null){
            arrayList.addAll(t);
        }
        return Single.just(arrayList);
    }

    public static <T> Single<ArrayList<T>> justArrayList(ArrayList<T> t){
        return t == null ? Single.just(new ArrayList<T>()) : Single.just(t);
    }


//    public static <T> Single<T> just(T t){
//        return t == null ? Single.<T>empty() : Single.just(t);
//    }
}
