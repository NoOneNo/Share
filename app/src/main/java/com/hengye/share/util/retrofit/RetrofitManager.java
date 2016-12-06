package com.hengye.share.util.retrofit;

import com.hengye.share.util.L;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.retrofit.api.ShareService;
import com.hengye.share.util.retrofit.api.WBService;
import com.hengye.share.util.retrofit.weibo.WBGsonConverterFactory;
import com.hengye.share.util.retrofit.weibo.WBNetworkInterceptor;
import com.hengye.share.util.retrofit.weibo.WBServiceProxyHandler;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.reflect.Proxy;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static Retrofit mWBRetrofit;

    private static WBService mWBService;
    private static ShareService mShareService;

    public static OkHttpClient mOkHttpClient;

    private static OkHttpClient getOkHttpClient(){
        if(mOkHttpClient == null) {
//            CustomLoggingInterceptor logging = new CustomLoggingInterceptor();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new OkHttpLog());
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new WBNetworkInterceptor())
                    .build();
        }
        return mOkHttpClient;
    }

    private static Retrofit getWBRetrofit(){
        if(mWBRetrofit == null) {
            mWBRetrofit = new Retrofit.Builder()
                    .baseUrl(UrlFactory.getWBUrlPrefix())
                    .addConverterFactory(WBGsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();

        }
        return mWBRetrofit;
    }

    public static ShareService getShareService(){
        if(mShareService == null){
            mShareService = new Retrofit.Builder()
                    .baseUrl("https://api.yuhy.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build()
                    .create(ShareService.class);
        }
        return mShareService;
    }

    public static WBService getWBService(){
        if(mWBService == null) {
//            WBService wbService = getWBRetrofit().create(WBService.class);
            mWBService = getWBServiceProxy();
        }
        return mWBService;
    }

    private static WBService getWBServiceProxy(){
        WBService wbService = getWBRetrofit().create(WBService.class);
        WBServiceProxyHandler mProxyHandler = new WBServiceProxyHandler(wbService);
        return (WBService) Proxy.newProxyInstance(WBService.class.getClassLoader(), new Class<?>[]{WBService.class}, mProxyHandler);
    }

    public static class OkHttpLog implements HttpLoggingInterceptor.Logger{

        @Override
        public void log(String message) {
            L.debug(message);
        }
    }

}
