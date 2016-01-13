package com.hengye.share.ui.presenter;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.google.gson.reflect.TypeToken;
import com.hengye.share.model.AtUser;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBUserInfos;
import com.hengye.share.ui.base.BasePresenter;
import com.hengye.share.ui.mvpview.AtUserMvpView;
import com.hengye.share.ui.mvpview.TemplateMvpView;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class AtUserPresenter extends BasePresenter<AtUserMvpView> {

    public AtUserPresenter(AtUserMvpView mvpView) {
        super(mvpView);
    }

    @Override
    public void attachView(AtUserMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public ArrayList<AtUser> getSelectResultData() {
        return new ArrayList<>();
    }

    public ArrayList<AtUser> getSearchResultData() {
        ArrayList<AtUser> searchResultData = SPUtil.getModule(new TypeToken<ArrayList<AtUser>>() {
        }.getType(), AtUser.class.getSimpleName() + UserUtil.getUid());

        if (searchResultData == null) {
            searchResultData = new ArrayList<>();
        }
        return searchResultData;
    }

    public void loadWBAttention(){
        RequestManager.addToRequestQueue(getWBAttentionRequest(UserUtil.getToken(), UserUtil.getUid()));
    }

    private GsonRequest getWBAttentionRequest(String token, String uid) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBAttentionUrl());
        ub.addParameter("access_token", token);
        ub.addParameter("uid", uid);
        ub.addParameter("count", 200);
//        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return new GsonRequest<>(
                WBUserInfos.class,
                ub.getRequestUrl(),
                new Response.Listener<WBUserInfos>() {
                    @Override
                    public void onResponse(WBUserInfos response) {
                        L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                        getMvpView().showSuccess(UserInfo.getUserInfos(response));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
                getMvpView().showFail();
            }

        });
    }
}
