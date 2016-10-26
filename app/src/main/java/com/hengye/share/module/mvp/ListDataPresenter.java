package com.hengye.share.module.mvp;

import com.hengye.share.module.util.encapsulation.paging.ListDataCallBack;

import java.util.List;

/**
 * Created by yuhy on 2016/10/20.
 */

public class ListDataPresenter<D, V extends MvpView & ListDataCallBack<D>> extends TaskPresenter<V> {

    public ListDataPresenter(V mvpView){
        super(mvpView);
    }

    public class ListDataSubscriber extends TaskSubscriber<List<D>>{

        public ListDataSubscriber(boolean isRefresh){
            super(isRefresh);
        }

        @Override
        public void onError(V v, Throwable e) {
            super.onError(v, e);
        }

        @Override
        public void onComplete(V v) {
            super.onComplete(v);
        }

        @Override
        public void onNext(V v, List<D> listData) {
            v.onLoadListData(isRefresh, listData);
        }
    }
}
