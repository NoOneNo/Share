package com.hengye.share.util.retrofit;

import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.retrofit.weibo.WBGsonConverterFactory;
import com.hengye.share.util.retrofit.weibo.WBService;
import com.hengye.share.util.retrofit.weibo.WBServiceProxyHandler;

import java.lang.reflect.Proxy;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static Retrofit mWBRetrofit;

    private static WBService mWBService;

    public static OkHttpClient mOkHttpClient;

    public static OkHttpClient getOkHttpClient(){
        if(mOkHttpClient == null) {
//            CustomLoggingInterceptor logging = new CustomLoggingInterceptor();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();
        }
        return mOkHttpClient;
    }

    public static Retrofit getWBRetrofit(){
        if(mWBRetrofit == null) {
            mWBRetrofit = new Retrofit.Builder()
                    .baseUrl(UrlFactory.getWBUrlPrefix())
                    .addConverterFactory(WBGsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();

        }
        return mWBRetrofit;
    }

    public static WBService getWBService(){
        if(mWBService == null) {
//            WBService wbService = getWBRetrofit().create(WBService.class);
            mWBService = getWBServiceProxy();
        }
        return mWBService;
    }

    public static WBService getWBServiceProxy(){
        WBService wbService = getWBRetrofit().create(WBService.class);
        WBServiceProxyHandler mProxyHandler = new WBServiceProxyHandler(wbService);
        return (WBService) Proxy.newProxyInstance(WBService.class.getClassLoader(), new Class<?>[]{WBService.class}, mProxyHandler);
    }

}
