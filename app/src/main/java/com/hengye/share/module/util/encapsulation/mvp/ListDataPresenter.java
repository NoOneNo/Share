package com.hengye.share.module.util.encapsulation.mvp;

import com.hengye.share.module.util.encapsulation.base.ListDataCallBack;

import java.util.List;

/**
 * Created by yuhy on 2016/10/20.
 */

public class ListDataPresenter<T, V extends MvpView & ListDataCallBack<T>> extends ListTaskPresenter<V> {

    public ListDataPresenter(V mvpView){
        super(mvpView);
    }

    public class ListDataObserver extends ListTaskObserver<List<T>> {

        public ListDataObserver(boolean isRefresh){
            super(isRefresh);
        }

        @Override
        public void onNext(V v, List<T> listData) {
            v.onLoadListData(isRefresh, listData);
        }
    }

    public class ListDataSingleObserver extends ListTaskSingleObserver<List<T>>{

        public ListDataSingleObserver(boolean isRefresh){
            super(isRefresh);
        }

        @Override
        public void onSuccess(V v, List<T> listData) {
            super.onSuccess(v, listData);
            v.onLoadListData(isRefresh, listData);
        }
    }
}
