package com.hengye.share.util.rxjava;

import rx.functions.Func2;

public class ObjectConverter {

    public static Object[] get(Object... objects){
        return objects;
    }

    private static Func2<Object, Object, Object[]> mObjectConverter2;

    public static Func2<Object, Object, Object[]> getObjectConverter2(){
        if(mObjectConverter2 == null){
            mObjectConverter2 = new Func2<Object, Object, Object[]>() {
                @Override
                public Object[] call(Object obj1, Object obj2) {
                    return ObjectConverter.get(obj1, obj2);
                }
            };
        }
        return mObjectConverter2;
    }
}
