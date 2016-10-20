package com.hengye.share.module.util.encapsulation.paging;

/**
 * Created by yuhy on 2016/10/20.
 */

public interface TaskCallBack {

    public void onTaskStart();

    public void onTaskComplete(boolean isRefresh, int taskState);
}
