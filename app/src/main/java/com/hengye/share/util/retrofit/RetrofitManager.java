package com.hengye.share.util.retrofit;

import com.hengye.share.util.UrlFactory;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

public class RetrofitManager {

    private static Retrofit mWBRetrofit;

    private static WBRetrofitService mWBRetrofitService;

    public static Retrofit getWBRetrofit(){
        if(mWBRetrofit == null) {
            mWBRetrofit = new Retrofit.Builder()
                    .baseUrl(UrlFactory.getInstance().getWBUrlPrefix())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return mWBRetrofit;
    }

    public static WBRetrofitService getWBRetrofitService(){
        if(mWBRetrofitService == null) {
            mWBRetrofitService = getWBRetrofit().create(WBRetrofitService.class);
        }
        return mWBRetrofitService;
    }
}
