package com.hengye.share.util.retrofit;

import com.hengye.share.util.UrlFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

public class RetrofitManager {

    private static Retrofit mWBRetrofit;

    private static WBService mWBService;

    public static OkHttpClient mOkHttpClient;

    public static OkHttpClient getOkHttpClient(){
        if(mOkHttpClient == null) {
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
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();

        }
        return mWBRetrofit;
    }

    public static WBService getWBService(){
        if(mWBService == null) {
            mWBService = getWBRetrofit().create(WBService.class);
        }
        return mWBService;
    }
}
