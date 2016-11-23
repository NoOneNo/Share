package com.hengye.share.util.rxjava;

import io.reactivex.functions.BiFunction;

public class ObjectConverter {

    public static Object[] get(Object... objects){
        return objects;
    }

    private static BiFunction<Object, Object, Object[]> mObjectConverter2;

    public static BiFunction<Object, Object, Object[]> getObjectConverter2(){
        if(mObjectConverter2 == null){
            mObjectConverter2 = new BiFunction<Object, Object, Object[]>() {
                @Override
                public Object[] apply(Object obj1, Object obj2) {
                    return get(obj1, obj2);
                }
            };
        }
        return mObjectConverter2;
    }
}
