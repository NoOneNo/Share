package com.hengye.share.ui.presenter;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.google.gson.reflect.TypeToken;
import com.hengye.share.handler.data.NumberPager;
import com.hengye.share.model.AtUser;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBUserInfos;
import com.hengye.share.ui.mvpview.UserListMvpView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.text.ChineseToPinyin;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserListPresenter extends BasePresenter<UserListMvpView> {

    private String mUid;
    private NumberPager mPager;

    public UserListPresenter(UserListMvpView mvpView, String uid) {
        super(mvpView);

        mUid = uid;
        mPager = new NumberPager(0);
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

    public void loadWBAttention() {
        RequestManager.addToRequestQueue(getWBAttentionRequest(UserUtil.getPriorToken(), mUid));
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
                        if (getMvpView() != null) {
                            getMvpView().stopLoading(true);

                            List<UserInfo> userInfos = UserInfo.getUserInfos(response);
                            if(!CommonUtil.isEmpty(userInfos)){
                                for(UserInfo ui : userInfos){
                                    ui.setSpell(ChineseToPinyin.getPinyin(ui.getName()));
                                }
                            }
                            getMvpView().showUserListSuccess(userInfos, true);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
                if (getMvpView() != null) {
                    getMvpView().stopLoading(true);
                    getMvpView().loadFail(true);
                }
            }

        });
    }

    public void loadWBAttentions(boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listAttentions(getWBUserListParameter(isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBUserListSubscriber(isRefresh));
    }

    public void loadWBFollowers(boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listFollowers(getWBUserListParameter(isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBUserListSubscriber(isRefresh));
    }

    public Map<String, String> getWBUserListParameter(boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getPriorToken());

        ub.addParameter("cursor", isRefresh ? mPager.getFirstPage() : mPager.getPageNumber());

        ub.addParameter("uid", mUid);

        ub.addParameter("count", WBUtil.getWBTopicRequestCount());
        return ub.getParameters();
    }

    private Subscriber<WBUserInfos> getWBUserListSubscriber(final boolean isRefresh) {
        return new BaseSubscriber<WBUserInfos>() {
            @Override
            public void handleViewOnFail(UserListMvpView v, Throwable e) {
                v.stopLoading(isRefresh);
                v.loadFail(isRefresh);
            }

            @Override
            public void handleViewOnSuccess(UserListMvpView v, WBUserInfos wbUserInfos) {
                v.stopLoading(isRefresh);
                v.showUserListSuccess(UserInfo.getUserInfos(wbUserInfos), isRefresh);
                mPager.setPageNumber(mPager.getPageNumber() + WBUtil.getWBTopicRequestCount());

                if(wbUserInfos == null || wbUserInfos.getNext_cursor() == 0) {
                    v.canLoadMore(false);
                }else{
                    v.canLoadMore(true);
                }
            }
        };
    }

}
