package com.hengye.share.module.util.encapsulation.base;

import java.util.List;

/**
 * Created by yuhy on 16/7/27.
 */
public interface DataAdapter<T> {

    boolean isEmpty();

    List<T> getData();

    void addAll(int position, List<T> data);

    void addAll(List<T> data);

    void refresh(List<T> data);

    void addItem(int position, T item);

    void addItem(T item);

    T removeItem(int position);

    T removeItem(T item);


}
