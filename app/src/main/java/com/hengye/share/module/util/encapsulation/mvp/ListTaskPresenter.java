package com.hengye.share.module.util.encapsulation.mvp;

import com.hengye.share.module.util.encapsulation.base.ListTaskCallBack;
import com.hengye.share.module.util.encapsulation.base.TaskState;

/**
 * Created by yuhy on 2016/10/20.
 */

public class ListTaskPresenter<V extends MvpView & ListTaskCallBack> extends RxPresenter<V> {

    public ListTaskPresenter(V mvpView){
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
            v.onTaskComplete(isRefresh, TaskState.getTaskFailState(e));
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
