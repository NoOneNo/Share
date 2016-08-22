package com.hengye.share.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommonUtil {

    /**
     * @param strings
     * @return 是否都没有空的值, 没有则返回true;
     */
    public static boolean noEmpty(String... strings) {
        return !hasEmpty(strings);
    }

    /**
     * @param strings
     * @return 是否有空的值, 有则返回true;
     */
    public static boolean hasEmpty(String... strings) {
        for (String str : strings) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * @param strings
     * @return 是否都是空的值, 是则返回true
     */
    public static boolean isEmpty(String... strings) {
        for (String str : strings) {
            if (!isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Collection collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean hasEmpty(Collection... collections) {
        for (Collection collection : collections) {
            if (isEmpty(collection)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(Map map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isEquals(String str1, String str2) {
        if (str1 == null) {
            return isEmpty(str2);
        }else{
            return str1.equals(str2);
        }
    }

    public static boolean hasEquals(int target, int... excepts){
        for(int except : excepts){
            if(target == except){
                return true;
            }
        }
        return false;
    }


    public static <T> T getLastItem(List<T> list) {
        return list.get(list.size() - 1);
    }

    public static int size(Collection collection){
        if(isEmpty(collection)){
            return 0;
        }
        return collection.size();
    }


    public interface ToSplit {
        String toSplit();
    }

    public static <T extends ToSplit> String toSplitAppointed(List<T> list, String separator) {
        if (isEmpty(list) || isEmpty(separator)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            String toSplit = t.toSplit();
            if (isEmpty(toSplit)) {
                continue;
            }
            sb.append(toSplit);
            if (i != list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static String toSplit(List list, String separator) {
        if (isEmpty(list) || isEmpty(separator)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String toSplit = list.get(i).toString();
            if (isEmpty(toSplit)) {
                continue;
            }
            sb.append(toSplit);
            if (i != list.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static List<String> split(String str, String separator) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return Arrays.asList(str.split(separator));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}









