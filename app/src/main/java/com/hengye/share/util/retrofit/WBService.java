package com.hengye.share.util.retrofit;

import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.model.sina.WBTopicComment;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicIds;
import com.hengye.share.model.sina.WBTopicReposts;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.model.sina.WBUserInfos;
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
    Observable<WBTopic> publishTopic
    (@Field("access_token") String token, @Field("status") String status);

    @FormUrlEncoded
    @POST(UrlFactory.WB_COMMENT_TOPIC)
    Observable<WBTopicComment> publishComment
            (@Field("access_token") String token,
             @Field("comment") String comment,
             @Field("id") String id,
             @Field("comment_ori") Integer isForOriginalTopic);

    @FormUrlEncoded
    @POST(UrlFactory.WB_REPOST_TOPIC)
    Observable<WBTopic> repostTopic
            (@Field("access_token") String token,
             @Field("status") String status,
             @Field("id") String id,
             @Field("is_comment") Integer isComment);

    @FormUrlEncoded
    @POST(UrlFactory.WB_COMMENT_REPLY)
    Observable<WBTopicComment> replyComment
            (@Field("access_token") String token,
             @Field("comment") String comment,
             @Field("id") String id,
             @Field("cid") String cid,
             @Field("comment_ori") Integer isForOriginalTopic);

    @GET(UrlFactory.WB_SEARCH_USER)
    Observable<WBUserInfos> searchUser(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_SEARCH_PUBLIC)
    Observable<WBTopics> searchPublic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_SEARCH_TOPIC)
    Observable<WBTopics> searchTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_FRIEND_TOPIC)
    Observable<WBTopics> listTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_FRIEND_TOPIC_IDS)
    Observable<WBTopicIds> listTopicIds(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_BILATERAL_TOPIC)
    Observable<WBTopics> listBilateralTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_BILATERAL_TOPIC_IDS)
    Observable<WBTopicIds> listBilateralTopicIds(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_GROUP_TOPIC)
    Observable<WBTopics> listGroupTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_GROUP_TOPIC_IDS)
    Observable<WBTopicIds> listGroupTopicIds(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_MENTION)
    Observable<WBTopicComments> listCommentAtMeTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_TOPIC_MENTION)
    Observable<WBTopics> listTopicAtMeTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_TO_ME)
    Observable<WBTopicComments> listCommentToMeTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_BY_ME)
    Observable<WBTopicComments> listCommentByMeTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_USER_TOPIC)
    Observable<WBTopics> listUserTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_USER_INFO)
    Observable<WBUserInfo> listUserInfo(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_GROUP)
    Observable<WBGroups> listGroups(@QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_GROUP_ORDER)
    Observable<WBGroups.WBGroupUpdateOrder> updateGroupOrder(
            @Field("access_token") String token,
            @Field("count") String count,
            @Field("list_ids") String ids);
}
