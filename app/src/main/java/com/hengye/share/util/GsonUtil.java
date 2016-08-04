package com.hengye.share.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class GsonUtil {
    private static Gson mGson = new Gson();

    private static Gson getInstance() {
        return mGson;
    }

    private GsonUtil() {}

    public static String toJson(Object src) {
        return getInstance().toJson(src);
    }

    public static <T> T fromJson(String json, Type typeOfT){
        try {
            return getInstance().fromJson(json, typeOfT);
        }catch (JsonSyntaxException e){
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz){
        try {
            return getInstance().fromJson(json, clazz);
        }catch (JsonSyntaxException e){
            e.printStackTrace();
        }
        return null;
    }

}
