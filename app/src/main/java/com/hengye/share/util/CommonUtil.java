package com.hengye.share.util;

import android.text.TextUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommonUtil {

    public static boolean hasEmpty(String... strings){
        for(String str : strings){
            if(TextUtils.isEmpty(str)){
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String... strings){
        for(String str : strings){
            if(!TextUtils.isEmpty(str)){
                return false;
            }
        }
        return true;
    }

    public static boolean isEmptyCollection(Collection collection){
        if(collection == null || collection.isEmpty()){
            return true;
        }
        return false;
    }

    public static boolean hasEmptyCollection(Collection... collections){
        for(Collection collection : collections){
            if(collection == null || collection.isEmpty()){
                return true;
            }
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
