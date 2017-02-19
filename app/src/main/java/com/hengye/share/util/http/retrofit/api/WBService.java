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
import com.hengye.share.model.sina.WBStatus;
import com.hengye.share.model.sina.WBStatusComment;
import com.hengye.share.model.sina.WBStatusComments;
import com.hengye.share.model.sina.WBStatusFavorites;
import com.hengye.share.model.sina.WBStatusIds;
import com.hengye.share.model.sina.WBStatusReposts;
import com.hengye.share.model.sina.WBStatuses;
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
    Single<WBStatusComments> listComment(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_SHOW_WITH_LIKE)
    Single<WBStatusComments> listCommentWithLike(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_HOT_COMMENT_SHOW_WITH_LIKE)
    Single<WBStatusComments> listHotCommentWithLike(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_REPOST_SHOW)
    Single<WBStatusReposts> listRepost(@QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_PUBLISH_STATUS)
    Single<WBStatus> publishStatus
    (@Field("access_token") String token,
     @Field("status") String status);


    @FormUrlEncoded
    @POST(UrlFactory.WB_PUBLISH_STATUS)
    Single<WBStatus> publishStatus
            (@FieldMap Map<String, Object> fieldMap);

    @FormUrlEncoded
    @POST(UrlFactory.WB_DESTROY_STATUS)
    Single<WBStatus> destroyStatus
            (@Field("access_token") String token,
             @Field("id") String id);
//    @Multipart
//    @POST(UrlFactory.WB_PUBLISH_STATUS_UPLOAD)
//    Observable<WBStatus> publishStatusWithSinglePhoto
//    (@Part("access_token") RequestBody token,
//     @Part("status") RequestBody status,
//     @Part MultipartBody.Part file);

    @Multipart
    @POST(UrlFactory.WB_PUBLISH_STATUS_UPLOAD)
    Single<WBStatus> publishStatusWithSinglePhoto
            (@PartMap Map<String, RequestBody> partyMap,
             @Part MultipartBody.Part file);


//    @FormUrlEncoded
//    @POST(UrlFactory.WB_PUBLISH_STATUS_PICTURE)
//    Observable<WBStatus> publishStatusWithMultiplePhoto
//            (@Field("access_token") String token, @Field("status") String status, @Field("pic_id") String pic_id);

    @FormUrlEncoded
    @POST(UrlFactory.WB_PUBLISH_STATUS_PICTURE)
    Single<WBStatus> publishStatusWithMultiplePhoto
            (@FieldMap Map<String, Object> fieldMap);

    @Multipart
    @POST(UrlFactory.WB_UPLOAD_PICTURE)
    Single<WBUploadPicture> uploadPicture
            (@Part("access_token") RequestBody token,
             @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(UrlFactory.WB_COMMENT_STATUS)
    Single<WBStatusComment> publishComment
            (@Field("access_token") String token,
             @Field("comment") String comment,
             @Field("id") String id,
             @Field("comment_ori") Integer isForOriginalStatus);

    @FormUrlEncoded
    @POST(UrlFactory.WB_REPOST_STATUS)
    Single<WBStatus> repostStatus
            (@Field("access_token") String token,
             @Field("status") String status,
             @Field("id") String id,
             @Field("is_comment") Integer isComment);

    @FormUrlEncoded
    @POST(UrlFactory.WB_COMMENT_REPLY)
    Single<WBStatusComment> replyComment
            (@Field("access_token") String token,
             @Field("comment") String comment,
             @Field("id") String id,
             @Field("cid") String cid,
             @Field("comment_ori") Integer isForOriginalStatus);

    @GET(UrlFactory.WB_SEARCH_USER)
    Single<WBUserInfos> searchUser(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_SEARCH_PUBLIC)
    Single<WBStatuses> searchPublic(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_SEARCH_TOPICS)
    Single<WBStatuses> searchStatus(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_FRIEND_STATUS)
    Single<WBStatuses> listStatus(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_FRIEND_STATUS_IDS)
    Single<WBStatusIds> listStatusIds(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_BILATERAL_STATUS)
    Single<WBStatuses> listBilateralStatus(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_BILATERAL_STATUS_IDS)
    Single<WBStatusIds> listBilateralStatusIds(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_FAVORITES_STATUS)
    Single<WBStatusFavorites> listFavoritesStatus(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_GROUP_STATUS)
    Single<WBStatuses> listGroupStatus(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_GROUP_STATUS_IDS)
    Single<WBStatusIds> listGroupStatusIds(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_MENTION)
    Single<WBStatusComments> listCommentAtMeStatus(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_STATUS_MENTION)
    Single<WBStatuses> listAtMeStatus(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_TO_ME)
    Single<WBStatusComments> listCommentToMeStatus(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_COMMENT_BY_ME)
    Single<WBStatusComments> listCommentByMeStatus(@QueryMap Map<String, String> options);

    @GET(UrlFactory.WB_USER_STATUS)
    Single<WBStatuses> listUserStatus(@QueryMap Map<String, String> options);

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
    Single<WBStatus> createStatusFavorited(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST(UrlFactory.WB_STATUS_FAVORITE_DESTROY)
    Single<WBStatus> destroyStatusFavorited(@FieldMap Map<String, String> options);

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
