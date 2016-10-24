package com.hengye.share.util;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.model.sina.WBUserInfo;

/**
 * 用于生成一些不需要更新UI的网络请求
 */
public class RequestFactory {

    /** Lazy initialization via inner-class holder. */
    private static class RequestFactoryHolder {
        /** A singleton instance. */
        private final static RequestFactory INSTANCE = new RequestFactory();
    }

    private RequestFactory() {
    }

    /**
     * @return a singleton instance of this stateless operator.
     */
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

}
