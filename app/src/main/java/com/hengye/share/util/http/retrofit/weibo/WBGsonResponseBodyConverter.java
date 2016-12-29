package com.hengye.share.util.http.retrofit.weibo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Converter;

/**
 * Created by yuhy on 16/8/2.
 */
final class WBGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    WBGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override public T convert(ResponseBody value) throws IOException {

        String json = value.string();
        checkApiException(json);
        JsonReader jsonReader = gson.newJsonReader(new StringReader(json));
        try {
            return adapter.read(jsonReader);
        } finally {
            Util.closeQuietly(jsonReader);
            value.close();
        }
    }

    private void checkApiException(String json) throws RuntimeException{
        try{
            WBApiException apiException = gson.fromJson(json, WBApiException.class);

            if(apiException != null && apiException.isLegal()){
                throw apiException;
            }
        }catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
}