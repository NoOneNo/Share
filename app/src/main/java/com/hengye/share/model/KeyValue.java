package com.hengye.share.model;

/**
 * Created by yuhy on 16/8/1.
 */
public class KeyValue<K, V> {

    K k;
    V v;

    public KeyValue(K k, V v){
        this.k = k;
        this.v = v;
    }

    public K getKey(){
        return k;
    }

    public V getValue(){
        return v;
    }

}
