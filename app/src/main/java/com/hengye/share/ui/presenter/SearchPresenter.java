package com.hengye.share.ui.presenter;

import android.text.TextUtils;

import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.model.sina.WBUserInfos;
import com.hengye.share.ui.mvpview.SearchMvpView;
import com.hengye.share.util.L;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.ObjectConverter;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.retrofit.WBService;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchPresenter extends BasePresenter<SearchMvpView> {

    public SearchPresenter(SearchMvpView mvpView){
        super(mvpView);
    }

    public void loadWBSearchContent(String content){
        if(TextUtils.isEmpty(content)){
            return;
        }

        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBSearchUserUrl());
        ub.addParameter("access_token", UserUtil.getToken());
        ub.addParameter("q", content);
        ub.addParameter("sid", "o_weico");

        WBService service = RetrofitManager.getWBService();
        Observable.zip(
                service.searchUser(ub.getParameters()),
                service.searchPublic(ub.getParameters()),
                ObjectConverter.getObjectConverter2())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Object[]>() {
                    @Override
                    public void handleViewOnFail(SearchMvpView v, Throwable e) {
                        v.loadFail();
                    }

                    @Override
                    public void handleViewOnSuccess(SearchMvpView v, Object[] objects) {
                        v.handleSearchUserData(UserInfo.getUserInfos((WBUserInfos) objects[0]));
                        v.handleSearchPublicData(Topic.getTopics((WBTopics) objects[1]));
                        v.loadSuccess();
                    }
                });
    }
}
