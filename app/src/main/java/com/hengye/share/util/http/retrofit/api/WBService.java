package com.hengye.share.util.http.retrofit.api;

import com.hengye.share.model.sina.WBAddresses;
import com.hengye.share.model.sina.WBAttitude;
import com.hengye.share.model.sina.WBAttitudes;
import com.hengye.share.model.sina.WBCards;
import com.hengye.share.model.sina.WBGroup;
import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.model.sina.WBHotSearch;
import com.hengye.share.model.sina.WBHotTopic;
import com.hengye.share.model.sina.WBResult;
import com.hengye.share.model.sina.WBShortUrls;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.model.sina.WBTopicComment;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicFavorites;
import com.hengye.share.model.sina.WBTopicIds;
import com.hengye.share.model.sina.WBTopicReposts;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.model.sina.WBUploadPicture;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.model.sina.WBUserInfos;
import com.hengye.share.util.UrlFactory;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface WBService {

    //    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    //    @Headers({"Content-Type: multipart/form-data"})

    @FormUrlEncoded
    @POST(UrlFactory.WB_OAUTH_TOKEN)
    Single<HashMap<String, String>> oauthToken(@FieldMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_SHOW)
    Single<WBTopicComments> listComment(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_SHOW_WITH_LIKE)
    Single<WBTopicComments> listCommentWithLike(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_HOT_COMMENT_SHOW_WITH_LIKE)
    Single<WBTopicComments> listHotCommentWithLike(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_REPOST_SHOW)
    Single<WBTopicReposts> listRepost(@QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_PUBLISH_TOPIC)
    Single<WBTopic> publishTopic
    (@Field("access_token") String token,
     @Field("status") String status);


    @FormUrlEncoded
    @POST(UrlFactory.WB_PUBLISH_TOPIC)
    Single<WBTopic> publishTopic
            (@FieldMap Map<String, Object> fieldMap);

    @FormUrlEncoded
    @POST(UrlFactory.WB_DESTROY_TOPIC)
    Single<WBTopic> destroyTopic
            (@Field("access_token") String token,
             @Field("id") String id);
//    @Multipart
//    @POST(UrlFactory.WB_PUBLISH_TOPIC_UPLOAD)
//    Observable<WBTopic> publishTopicWithSinglePhoto
//    (@Part("access_token") RequestBody token,
//     @Part("status") RequestBody status,
//     @Part MultipartBody.Part file);

    @Multipart
    @POST(UrlFactory.WB_PUBLISH_TOPIC_UPLOAD)
    Single<WBTopic> publishTopicWithSinglePhoto
            (@PartMap Map<String, RequestBody> partyMap,
             @Part MultipartBody.Part file);


//    @FormUrlEncoded
//    @POST(UrlFactory.WB_PUBLISH_TOPIC_PICTURE)
//    Observable<WBTopic> publishTopicWithMultiplePhoto
//            (@Field("access_token") String token, @Field("status") String status, @Field("pic_id") String pic_id);

    @FormUrlEncoded
    @POST(UrlFactory.WB_PUBLISH_TOPIC_PICTURE)
    Single<WBTopic> publishTopicWithMultiplePhoto
            (@FieldMap Map<String, Object> fieldMap);

    @Multipart
    @POST(UrlFactory.WB_UPLOAD_PICTURE)
    Single<WBUploadPicture> uploadPicture
            (@Part("access_token") RequestBody token,
             @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(UrlFactory.WB_COMMENT_TOPIC)
    Single<WBTopicComment> publishComment
            (@Field("access_token") String token,
             @Field("comment") String comment,
             @Field("id") String id,
             @Field("comment_ori") Integer isForOriginalTopic);

    @FormUrlEncoded
    @POST(UrlFactory.WB_REPOST_TOPIC)
    Single<WBTopic> repostTopic
            (@Field("access_token") String token,
             @Field("status") String status,
             @Field("id") String id,
             @Field("is_comment") Integer isComment);

    @FormUrlEncoded
    @POST(UrlFactory.WB_COMMENT_REPLY)
    Single<WBTopicComment> replyComment
            (@Field("access_token") String token,
             @Field("comment") String comment,
             @Field("id") String id,
             @Field("cid") String cid,
             @Field("comment_ori") Integer isForOriginalTopic);

    @GET(UrlFactory.WB_SEARCH_USER)
    Single<WBUserInfos> searchUser(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_SEARCH_PUBLIC)
    Single<WBTopics> searchPublic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_SEARCH_TOPIC)
    Single<WBTopics> searchTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_FRIEND_TOPIC)
    Single<WBTopics> listTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_FRIEND_TOPIC_IDS)
    Single<WBTopicIds> listTopicIds(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_BILATERAL_TOPIC)
    Single<WBTopics> listBilateralTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_BILATERAL_TOPIC_IDS)
    Single<WBTopicIds> listBilateralTopicIds(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_FAVORITES_TOPIC)
    Single<WBTopicFavorites> listFavoritesTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_GROUP_TOPIC)
    Single<WBTopics> listGroupTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_GROUP_TOPIC_IDS)
    Single<WBTopicIds> listGroupTopicIds(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_MENTION)
    Single<WBTopicComments> listCommentAtMeTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_TOPIC_MENTION)
    Single<WBTopics> listTopicAtMeTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_TO_ME)
    Single<WBTopicComments> listCommentToMeTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_BY_ME)
    Single<WBTopicComments> listCommentByMeTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_USER_TOPIC)
    Single<WBTopics> listUserTopic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_USER_INFO)
    Single<WBUserInfo> listUserInfo(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_GROUP)
    Single<WBGroups> listGroups(@QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_GROUP_CREATE)
    Single<WBGroup> createGroup(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_GROUP_DESTROY)
    Single<WBGroup> destroyGroup(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_GROUP_UPDATE)
    Single<WBGroup> updateGroup(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_GROUP_ORDER)
    Single<WBGroups.WBGroupUpdateOrder> updateGroupOrder(
            @Field("access_token") String token,
            @Field("count") String count,
            @Field("list_ids") String ids);

    @FormUrlEncoded
    @POST(UrlFactory.WB_FOLLOW_CREATE)
    Single<WBUserInfo> createFollow(
            @Field("access_token") String token,
            @Field("uid") String count);

    @FormUrlEncoded
    @POST(UrlFactory.WB_FOLLOW_DESTROY)
    Single<WBUserInfo> destroyFollow(
            @Field("access_token") String token,
            @Field("uid") String count);


    @GET(UrlFactory.WB_USER_ATTENTIONS)
    Single<WBUserInfos> listAttentions(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_USER_FOLLOWERS)
    Single<WBUserInfos> listFollowers(@QueryMap Map<String, String> options);

//    @GET(UrlFactory.WB_EXPAND_URL)
//    Call<WBShortUrls> expandUrl(@QueryMap Map<String, String> options);

    /**
     * @param url 因为url参数含有多个同样的shortUrl键值，如果用@QueryMap就不能出现重复的键值了
     * @return
     */
    @GET
    Call<WBShortUrls> expandUrl(@Url String url);

    @FormUrlEncoded
    @POST("http://weicoapi.weico.cc/portal.php?a=get_video&c=default")
    Single<Object> getMediaPlayUrl(@Field("weibo_id") String weiboId);

    @FormUrlEncoded
    @POST(UrlFactory.WB_STATUS_FAVORITE_CREATE)
    Single<WBTopic> createStatusFavorited(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_STATUS_FAVORITE_DESTROY)
    Single<WBTopic> destroyStatusFavorited(@FieldMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_LIKE)
    Single<WBResult> createCommentLike(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_DISLIKE)
    Single<WBResult> destroyCommentLike(@QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_STATUS_LIKE)
    Single<WBAttitude> createStatusLike(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_STATUS_DISLIKE)
    Single<WBResult> destroyStatusLike(@FieldMap Map<String, String> options);

    @GET(UrlFactory.WB_STATUS_LIKE_SHOW)
    Single<WBAttitudes> listStatusLike(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_PLACE_NEARBY)
    Single<WBAddresses> getPlaceNearBy(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_HOT_STATUS)
    Single<WBCards> listHotStatus(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_HOT_TOPIC)
    Single<WBHotTopic> listHotTopic();

    @GET(UrlFactory.WB_HOT_SEARCH)
    Single<WBHotSearch> listHotSearch();
}
