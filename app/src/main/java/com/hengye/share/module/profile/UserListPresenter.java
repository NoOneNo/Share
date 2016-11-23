package com.hengye.share.module.profile;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.google.gson.reflect.TypeToken;
import com.hengye.share.model.AtUser;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBUserInfos;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.handler.TopicNumberPager;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.text.ChineseToPinyin;
import com.hengye.share.util.thirdparty.WBUtil;

import io.reactivex.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserListPresenter extends RxPresenter<UserListMvpView> {

    private String mUid;
    private TopicNumberPager mPager;

    public UserListPresenter(UserListMvpView mvpView, String uid) {
        super(mvpView);

        mUid = uid;
        mPager = new TopicNumberPager(0);
    }

    public ArrayList<AtUser> getSelectResultData() {
        return new ArrayList<>();
    }

    public ArrayList<AtUser> getSearchResultData() {
        ArrayList<AtUser> searchResultData = ShareJson.findData(AtUser.class.getSimpleName() + UserUtil.getUid(), new TypeToken<ArrayList<AtUser>>() {
        }.getType());

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
                            getMvpView().onTaskComplete(true, true);

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
                    getMvpView().onTaskComplete(true, true);
                }
            }

        });
    }

    public void loadWBAttentions(boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listAttentions(getWBUserListParameter(isRefresh))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getWBUserListSubscriber(isRefresh));
    }

    public void loadWBFollowers(boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listFollowers(getWBUserListParameter(isRefresh))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
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

    private Observer<WBUserInfos> getWBUserListSubscriber(final boolean isRefresh) {
        return new BaseSubscriber<WBUserInfos>() {
            @Override
            public void onError(UserListMvpView v, Throwable e) {
                v.onTaskComplete(isRefresh, false);
            }

            @Override
            public void onNext(UserListMvpView v, WBUserInfos wbUserInfos) {
                v.onTaskComplete(isRefresh, true);
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
