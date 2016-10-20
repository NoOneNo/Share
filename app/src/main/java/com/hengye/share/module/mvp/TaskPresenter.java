package com.hengye.share.module.mvp;

import com.hengye.share.module.util.encapsulation.paging.TaskCallBack;
import com.hengye.share.module.util.encapsulation.paging.TaskState;

/**
 * Created by yuhy on 2016/10/20.
 */

public class TaskPresenter<V extends MvpView & TaskCallBack> extends BasePresenter<V> {

    public TaskPresenter(V mvpView){
        super(mvpView);
    }

    public class TaskSubscriber<T> extends BaseSubscriber<T>{

        protected boolean isRefresh;

        public TaskSubscriber(boolean isRefresh){
            this.isRefresh = isRefresh;
        }

        @Override
        public void onError(V v, Throwable e) {
            super.onError(v, e);
            //根据e来判断是网络异常还是服务器异常;
            v.onTaskComplete(isRefresh, TaskState.STATE_FAIL_BY_NETWORK);
        }

        @Override
        public void onComplete(V v) {
            super.onComplete(v);
            v.onTaskComplete(isRefresh, TaskState.STATE_SUCCESS);
        }

        @Override
        public void onNext(V v, T list) {
        }
    }
}
