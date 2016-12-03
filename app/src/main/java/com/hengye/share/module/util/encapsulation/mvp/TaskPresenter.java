package com.hengye.share.module.util.encapsulation.mvp;

import com.hengye.share.module.util.encapsulation.base.TaskCallBack;
import com.hengye.share.module.util.encapsulation.base.TaskState;

/**
 * Created by yuhy on 2016/10/20.
 */

public class TaskPresenter<V extends MvpView & TaskCallBack> extends RxPresenter<V> {

    public TaskPresenter(V mvpView){
        super(mvpView);
    }

    public class TaskSubscriber<T> extends BaseSubscriber<T>{


        public TaskSubscriber(){
        }

        @Override
        public void onError(V v, Throwable e) {
            super.onError(v, e);
            //根据e来判断是网络异常还是服务器异常;
            v.onTaskComplete(TaskState.getFailState(e));
        }

        @Override
        public void onComplete(V v) {
            super.onComplete(v);
            v.onTaskComplete(TaskState.STATE_SUCCESS);
        }

        @Override
        public void onNext(V v, T list) {
        }
    }
}
