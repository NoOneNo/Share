package com.hengye.share.util;

import java.util.List;
import java.util.Map;

public class CommonUtil {

    public static boolean isEmptyList(List list){
        if(list == null || list.isEmpty()){
            return true;
        }
        return false;
    }

    public static boolean isEmptyMap(Map map){
        if(map == null || map.isEmpty()){
            return true;
        }
        return false;
    }

    public static <T> T getLastItem(List<T> list){
        return list.get(list.size() - 1);
    }

}
