package com.hengye.share.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommonUtil {

    public static boolean isEmptyCollection(Collection collection){
        if(collection == null || collection.isEmpty()){
            return true;
        }
        return false;
    }

    public static boolean isEmptyCollection(Collection... collections){
        for(Collection collection : collections){
            if(collection == null || collection.isEmpty()){
                return true;
            }
        }
        return false;
    }

//    public static boolean isEmptyColection(Collection collection){
//        if(collection == null || collection.isEmpty()){
//            return true;
//        }
//        return false;
//    }

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
