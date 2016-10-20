package com.hengye.share.module.util.encapsulation.paging;

/**
 * Created by yuhy on 2016/10/20.
 */

public class TaskState {

    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAIL_BY_NETWORK = 2;
    public static final int STATE_FAIL_BY_SERVER = 3;

    public static boolean isSuccess(int taskState){
        return taskState == STATE_SUCCESS;
    }

    public static boolean isFailByNetwork(int taskState){
        return taskState == STATE_FAIL_BY_NETWORK;
    }

    public static boolean isFailByServer(int taskState){
        return taskState == STATE_FAIL_BY_SERVER;
    }
}
