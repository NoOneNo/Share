package com.hengye.share.module.util.encapsulation.base;

import com.hengye.share.R;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by yuhy on 2016/10/20.
 */

public class TaskState {

    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAIL_BY_NETWORK = 2;
    public static final int STATE_FAIL_BY_SERVER = 3;

    public static int getFailState(Throwable throwable){
        return isNetworkException(throwable) ? TaskState.STATE_FAIL_BY_NETWORK : TaskState.STATE_FAIL_BY_SERVER;
    }

    public static boolean isSuccess(int taskState){
        return taskState == STATE_SUCCESS;
    }

    public static boolean isFailByNetwork(int taskState){
        return taskState == STATE_FAIL_BY_NETWORK;
    }

    public static boolean isFailByServer(int taskState){
        return taskState == STATE_FAIL_BY_SERVER;
    }

    public static boolean isNetworkException(Throwable throwable){
        return throwable instanceof UnknownHostException
                || throwable instanceof SocketTimeoutException
                || throwable instanceof ConnectException;
        //// TODO: 2016/11/29 ConnectException可能要去掉
    }

    public static String toString(int taskState){
        int resId;
        switch (taskState){
            case STATE_SUCCESS:
                default:
                resId = R.string.tip_load_success;
                break;
            case STATE_FAIL_BY_NETWORK:
                resId = R.string.tip_no_network;
                break;
            case STATE_FAIL_BY_SERVER:
                resId = R.string.tip_service_error;
                break;
        }
        return ResUtil.getString(resId);
    }

    public static void toastState(int taskState){
        switch (taskState){
            case STATE_SUCCESS:
            default:
                ToastUtil.showToastSuccess(toString(taskState));
                break;
            case STATE_FAIL_BY_NETWORK:
                ToastUtil.showToastWarning(toString(taskState));
                break;
            case STATE_FAIL_BY_SERVER:
                ToastUtil.showToastError(toString(taskState));
                break;
        }
    }

    public static void toastFailState(int taskState){
        if(taskState != STATE_SUCCESS){
            toastState(taskState);
        }
    }
}
