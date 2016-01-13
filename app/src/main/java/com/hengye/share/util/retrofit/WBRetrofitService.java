package com.hengye.share.util.retrofit;

import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicReposts;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface WBRetrofitService {

    //weibo url prefix
    String URL_PREFIX_WEIBO = "https://api.weibo.com/2/";


    @GET("comments/show.json")
    Observable<WBTopicComments> listComment(@QueryMap Map<String, String> options);

    @GET("statuses/repost_timeline.json")
    Observable<WBTopicReposts> listRepost(@QueryMap Map<String, String> options);


//    @GET("comments/show.json")
//    Call<WBTopicComments> listComment(@QueryMap Map<String, String> options);

//    @GET("statuses/repost_timeline.json")
//    Call<WBTopicReposts> listRepost(@QueryMap Map<String, String> options);
}
