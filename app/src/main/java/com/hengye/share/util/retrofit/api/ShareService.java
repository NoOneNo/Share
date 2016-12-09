package com.hengye.share.util.retrofit.api;

import com.hengye.share.model.other.AMapAddresses;
import com.hengye.share.module.update.UpdateBombBean;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import io.reactivex.Observable;
import retrofit2.http.QueryMap;

/**
 * Created by yuhy on 2016/11/21.
 */

public interface ShareService {

    @Headers({
        "X-Bmob-Application-Id: 49c7853c8483552c1ce7c0490092f83d",
        "X-Bmob-REST-API-Key: bb229d0f787dd1a4be3fcf140a76b554",
    })
    @GET("https://api.bmob.cn/1/classes/Android_Version?order=-versionCode&limit=1")
    Observable<UpdateBombBean> checkUpdate();

    @GET("https://restapi.amap.com/v3/place/around")
    Observable<AMapAddresses> getPlaceRoundByAMap(@QueryMap Map<String, String> options);
}
