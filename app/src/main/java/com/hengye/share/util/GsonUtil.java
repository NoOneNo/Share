package com.hengye.share.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class GsonUtil {
    private static Gson mGson = new Gson();

    public static Gson getInstance() {
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

}
