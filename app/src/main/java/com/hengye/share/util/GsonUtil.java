package com.hengye.share.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class GsonUtil {
    private static Gson mGson = new Gson();
    private static JsonParser mJsonParser = new JsonParser();

    private GsonUtil() {}

    public static Gson getInstance() {
        return mGson;
    }

    public static JsonObject toJsonObject(Object obj){
        try {
            return mJsonParser.parse(toJson(obj)).getAsJsonObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Object obj) {
        return getInstance().toJson(obj);
    }

    /**
     * 有泛型时传Type
     */
    public static String toJson(Object obj, Type typeOfT){
        return getInstance().toJson(obj, typeOfT);
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
