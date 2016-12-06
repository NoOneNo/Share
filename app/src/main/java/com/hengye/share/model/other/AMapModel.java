package com.hengye.share.model.other;

/**
 * Created by yuhy on 2016/12/6.
 */

public class AMapModel {

    public static final String CODE_ERROR = "0";
    public static final String CODE_SUCCESS = "1";

    public static boolean isSucess(String status){
        return CODE_SUCCESS.equals(status);
    }
}
