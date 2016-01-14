package com.hengye.share.util.retrofit;

import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicReposts;
import com.hengye.share.util.UrlFactory;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface WBService {

    @GET(UrlFactory.WB_COMMENT_SHOW)
    Observable<WBTopicComments> listComment(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_REPOST_SHOW)
    Observable<WBTopicReposts> listRepost(@QueryMap Map<String, String> options);


//    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(UrlFactory.WB_PUBLISH_TOPIC)
    Observable<WBTopic> publishTopic(
            @Field("status") String status, @Field("access_token") String token);
}
