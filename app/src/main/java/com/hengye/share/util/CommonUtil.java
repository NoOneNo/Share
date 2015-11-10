package com.hengye.share.util;

import java.util.List;

public class CommonUtil {

    public static boolean isEmptyList(List list){
        if(list == null || list.isEmpty()){
            return true;
        }
        return false;
    }
}
