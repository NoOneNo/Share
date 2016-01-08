package com.hengye.share.util;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.model.Parent;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBUserInfo;

/**
 * 用于生成一些不需要更新UI的网络请求
 */
public class RequestFactory {

    private static class RequestFactoryHolder {
        private final static RequestFactory INSTANCE = new RequestFactory();
    }

    private RequestFactory() {
    }

    public static RequestFactory getInstance() {
        return RequestFactoryHolder.INSTANCE;
    }


    public GsonRequest getWBUserInfoRequest(String token, final String uid) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBUserInfoUrl());
        ub.addParameter("access_token", token);
        ub.addParameter("uid", uid);
        return new GsonRequest<>(
                WBUserInfo.class,
                ub.getRequestUrl(),
                new Response.Listener<WBUserInfo>() {
            @Override
            public void onResponse(WBUserInfo response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);

                UserUtil.updateUserInfo(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
            }

        });
    }

//    public GsonRequest<WBTopics> getWBTopicRequest(String token,
//                                                  Response.Listener<WBTopics> listener, Response.ErrorListener errorListener) {
//        return getWBTopicRequest(token, 0, true, 1, listener, errorListener);
//    }
//
//
//    public GsonRequest<WBTopics> getWBTopicRequest(String token, int id, boolean isRefresh,
//                                                  Response.Listener<WBTopics> listener, Response.ErrorListener errorListener) {
//        return getWBTopicRequest(token, id, isRefresh, 1, listener, errorListener);
//    }
//
//    public GsonRequest<WBTopics> getWBTopicRequest(String token, int id, boolean isRefresh, int page,
//                                                  Response.Listener<WBTopics> listener, Response.ErrorListener errorListener) {
//        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBFriendTopicIdsUrl());
//        ub.addParameter("access_token", token);
//        if(isRefresh){
//            ub.addParameter("since_id", id);
//        }else{
//            ub.addParameter("max_id", id);
//        }
//        ub.addParameter("page", page);
//        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
//        return new GsonRequest<>(
//                ub.getRequestUrl()
//                , WBTopics.class
//                , listener, errorListener);
//    }

//    public GsonRequest<WBTopicIds> getWBUnReadIdsRequest(String token, String since_id,
//                                                         Response.Listener<WBTopicIds> listener, Response.ErrorListener errorListener) {
//        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBFriendTopicIdsUrl());
//        ub.addParameter("access_token", token);
//        ub.addParameter("since_id", since_id);
//        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
//        return new GsonRequest<>(
//                ub.getRequestUrl()
//                , WBTopicIds.class
//                , listener, errorListener);
//    }

}
