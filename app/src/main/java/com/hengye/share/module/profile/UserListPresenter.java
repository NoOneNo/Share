package com.hengye.share.module.profile;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.model.AtUser;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBUserInfos;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.handler.TopicNumberPager;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.text.ChineseToPinyin;
import com.hengye.share.util.thirdparty.WBUtil;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserListPresenter extends ListDataPresenter<UserInfo, UserListContract.View> {

    private String mUid;
    private TopicNumberPager mPager;

    public UserListPresenter(UserListContract.View mvpView, String uid) {
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

//    public void loadWBAttention() {
//        RequestManager.addToRequestQueue(getWBAttentionRequest(UserUtil.getPriorToken(), mUid));
//    }
//
//    private GsonRequest getWBAttentionRequest(String token, String uid) {
//        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBAttentionUrl());
//        ub.addParameter("access_token", token);
//        ub.addParameter("uid", uid);
//        ub.addParameter("count", 200);
////        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
//        return new GsonRequest<>(
//                WBUserInfos.class,
//                ub.getRequestUrl(),
//                new Response.Listener<WBUserInfos>() {
//                    @Override
//                    public void onResponse(WBUserInfos response) {
//                        L.debug("request success , url : %s, data : %s", ub.getRequestUrl(), response);
//                        if (getMvpView() != null) {
//                            getMvpView().onTaskComplete(true, true);
//
//                            List<UserInfo> userInfos = UserInfo.getUserInfos(response);
//                            if(!CommonUtil.isEmpty(userInfos)){
//                                for(UserInfo ui : userInfos){
//                                    ui.setSpell(ChineseToPinyin.getPinyin(ui.getName()));
//                                }
//                            }
//                            getMvpView().showUserListSuccess(userInfos, true);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                L.debug("request fail , url : %s, error : %s", ub.getRequestUrl(), error);
//                if (getMvpView() != null) {
//                    getMvpView().onTaskComplete(true, true);
//                }
//            }
//
//        });
//    }

    public void loadWBAttentions(boolean isRefresh) {
        loadWBAttentions(isRefresh, false);
    }


    public void loadWBAttentions(boolean isRefresh, final boolean loadAll) {
        RetrofitManager
                .getWBService()
                .listAttentions(getWBUserListParameter(isRefresh, loadAll))
                .flatMap(flatWBUserInfos(loadAll))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getUserListSubscriber(isRefresh));
    }

    public void loadWBFollowers(boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listFollowers(getWBUserListParameter(isRefresh))
                .flatMap(flatWBUserInfos(false))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getUserListSubscriber(isRefresh));
    }

    private Map<String, String> getWBUserListParameter(boolean isRefresh) {
        return getWBUserListParameter(isRefresh, false);
    }

    private Map<String, String> getWBUserListParameter(boolean isRefresh, boolean loadAll) {
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getPriorToken());

        ub.addParameter("cursor", isRefresh ? mPager.getFirstPage() : mPager.getPageNumber());

        ub.addParameter("uid", mUid);

        ub.addParameter("count", loadAll ? 200 : WBUtil.getWBTopicRequestCount());
        return ub.getParameters();
    }

    private Function<WBUserInfos, SingleSource<ArrayList<UserInfo>>> flatWBUserInfos(final boolean loadAll) {
        return new Function<WBUserInfos, SingleSource<ArrayList<UserInfo>>>() {
            @Override
            public SingleSource<ArrayList<UserInfo>> apply(WBUserInfos wbUserInfos) throws Exception {

                ArrayList<UserInfo> userInfos = UserInfo.getUserInfos(wbUserInfos);

                if (loadAll) {
                    if (!CommonUtil.isEmpty(userInfos)) {
                        for (UserInfo ui : userInfos) {
                            ui.setSpell(ChineseToPinyin.getPinyin(ui.getName()));
                        }
                    }
                }
                return SingleHelper.justArrayList(userInfos);
            }
        };
    }

    private SingleObserver<List<UserInfo>> getUserListSubscriber(final boolean isRefresh) {
        return new ListDataSingleObserver(isRefresh){
            @Override
            public void onSuccess(UserListContract.View view, List<UserInfo> listData) {
                super.onSuccess(view, listData);
                mPager.setPageNumber(mPager.getPageNumber() + WBUtil.getWBTopicRequestCount());
            }
        };
    }

}
