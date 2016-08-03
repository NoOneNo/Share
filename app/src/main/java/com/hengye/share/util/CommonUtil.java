package com.hengye.share.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommonUtil {

    public static boolean noEmpty(String... strings) {
        return !hasEmpty(strings);
    }

    public static boolean hasEmpty(String... strings) {
        for (String str : strings) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        if (!TextUtils.isEmpty(str)) {
            return false;
        }
        return true;
    }

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
            return str2 == null;
        }else{
            return str1.equals(str2);
        }
    }

    public static <T> T getLastItem(List<T> list) {
        return list.get(list.size() - 1);
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









