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

    public class TaskSingleObserver<T> extends BaseSingleObserver<T>{

        @Override
        public void onSuccess(T t) {
            super.onSuccess(t);
            //在处理完数据后再调用onTaskComplete()方法处理数据切换view的显示
            if(isViewAttached()){
                getMvpView().onTaskComplete(TaskState.STATE_SUCCESS);
            }
        }

        @Override
        public void onSuccess(V v, T t) {}

        @Override
        public void onError(V v, Throwable e) {
            v.onTaskComplete(TaskState.getFailState(e));
        }
    }
}
