package com.hengye.share.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CommonUtil {

    public static final String UNDERLINE = "_";

    /**
     * @param strings
     * @return 是否都没有空的值, 没有则返回true;
     */
    public static boolean noEmpty(CharSequence... strings) {
        return !hasEmpty(strings);
    }

    /**
     * @param strings
     * @return 是否有空的值, 有则返回true;
     */
    public static boolean hasEmpty(CharSequence... strings) {
        for (CharSequence str : strings) {
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
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * @param strings
     * @return 是否都是空的值, 是则返回true
     */
    public static boolean isEmpty(CharSequence... strings) {
        for (CharSequence str : strings) {
            if (!isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Iterable iterable) {
        return iterable == null || iterable.iterator() == null || !iterable.iterator().hasNext();
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
        return map == null || map.isEmpty();
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

    public static <T extends ToSplit> String toSplitAppointed(List<T> list, CharSequence delimiter) {
        if (isEmpty(list) || isEmpty(delimiter)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (T token: list) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token.toSplit());
        }
        return sb.toString();
    }

    public static String toSplit(Iterable tokens, CharSequence delimiter) {
        if (isEmpty(tokens) || isEmpty(delimiter)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token: tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static ArrayList<String> split(String str, String delimiter) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return new ArrayList<>(Arrays.asList(str.split(delimiter)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getLongValue(String value){
        if(value == null){
            return 0L;
        }
        try {
            return Long.valueOf(value);
        }catch (NumberFormatException nfe){
            nfe.printStackTrace();
            return 0L;
        }
    }
}









