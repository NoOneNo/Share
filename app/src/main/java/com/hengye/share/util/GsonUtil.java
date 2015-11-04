package com.hengye.share.util;

import com.google.gson.Gson;

public class GsonUtil {
    private static Gson mGson = new Gson();

    public static Gson getInstance() {
        return mGson;
    }

    private GsonUtil() {}
}
