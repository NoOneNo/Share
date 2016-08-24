package com.hengye.share.util.retrofit.weibo;

import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.model.sina.WBTopicComment;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicIds;
import com.hengye.share.model.sina.WBTopicReposts;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.model.sina.WBUploadPicture;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.model.sina.WBUserInfos;
import com.hengye.share.util.UrlFactory;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface WBService {

    @FormUrlEncoded
    @POST(UrlFactory.WB_OAUTH_TOKEN)
    Observable<HashMap<String, String>> oauthToken(@FieldMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_SHOW)
    Observable<WBTopicComments> listComment(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_REPOST_SHOW)
    Observable<WBTopicReposts> listRepost(@QueryMap Map<String, String> options);

    //    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST(UrlFactory.WB_PUBLISH_TOPIC)
    Observable<WBTopic> publishTopic
    (@Field("access_token") String token, @Field("status") String status);

    //    @Headers({"Content-Type: multipart/form-data"})
    @Multipart
    @POST(UrlFactory.WB_PUBLISH_TOPIC_UPLOAD)
    Observable<WBTopic> publishTopicWithSinglePhoto
    (@Part("access_token") RequestBody token,
     @Part("status") RequestBody status,
     @Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST(UrlFactory.WB_PUBLISH_TOPIC_PICTURE)
    Observable<WBTopic> publishTopicWithMultiplePhoto
            (@Field("access_token") String token, @Field("status") String status, @Field("pic_id") String pic_id);

    @Multipart
    @POST(UrlFactory.WB_UPLOAD_PICTURE)
    Observable<WBUploadPicture> uploadPicture
            (@Part("access_token") RequestBody token,
             @Part MultipartBody.Part file);

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

    @GET(UrlFactory.WB_FAVORITES_TOPIC)
    Observable<WBTopics> listFavoritesTopic(@QueryMap Map<String, String> options);

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

    @FormUrlEncoded
    @POST(UrlFactory.WB_FOLLOW_CREATE)
    Observable<WBUserInfo> followCreate(
            @Field("access_token") String token,
            @Field("uid") String count);

    @FormUrlEncoded
    @POST(UrlFactory.WB_FOLLOW_DESTROY)
    Observable<WBUserInfo> followDestroy(
            @Field("access_token") String token,
            @Field("uid") String count);


    @GET(UrlFactory.WB_USER_ATTENTIONS)
    Observable<WBUserInfos> listAttentions(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_USER_FOLLOWERS)
    Observable<WBUserInfos> listFollowers(@QueryMap Map<String, String> options);
}
