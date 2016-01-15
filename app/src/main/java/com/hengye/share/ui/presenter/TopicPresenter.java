package com.hengye.share.ui.presenter;

import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBTopicIds;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.base.BasePresenter;
import com.hengye.share.ui.mvpview.TopicMvpView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TopicPresenter extends BasePresenter<TopicMvpView> {

    public TopicPresenter(TopicMvpView mvpView) {
        super(mvpView);
    }

    public void loadWBTopic(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listTopic(getWBTopicParameter(id, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicsSubscriber(isRefresh));
    }

    public void loadWBTopicIds(final String since_id) {
        RetrofitManager
                .getWBService()
                .listTopicIds(getWBTopicParameter(since_id, true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WBTopicIds>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().stopLoading(true);
                    }

                    @Override
                    public void onNext(WBTopicIds wbTopicIds) {
                        if (wbTopicIds == null || CommonUtil.isEmptyCollection(wbTopicIds.getStatuses())) {
                            //没有新的微博
                            getMvpView().stopLoading(true);
                            getMvpView().handleNoMoreTopics();
                            L.debug("no topic update");
                        } else {
                            if (wbTopicIds.getStatuses().size() >= WBUtil.MAX_COUNT_REQUEST) {
                                //还有更新的微博，重新请求刷新
//                                RequestManager.addToRequestQueue(getWBTopicRequest(token, 0 + "", true), getRequestTag());
                                L.debug("exist newer topic, request refresh again");
                                loadWBTopic("0", true);
                            } else {
                                //新的微博条数没有超过请求条数，显示更新多少条微博，根据请求的since_id获取微博
//                                RequestManager.addToRequestQueue(getWBTopicRequest(token, since_id, true), getRequestTag());
                                L.debug("new topic is less than MAX_COUNT_REQUEST, request refresh by since_id");
                                loadWBTopic(since_id, true);
                            }
                        }
                    }
                });

    }

    public void loadWBUserInfo(){
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBUserInfoUrl());
        ub.addParameter("access_token", UserUtil.getToken());
        ub.addParameter("uid", UserUtil.getUid());

        RetrofitManager
                .getWBService()
                .listUserInfo(ub.getParameters())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WBUserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WBUserInfo wbUserInfo) {
                        UserUtil.updateUserInfo(wbUserInfo);
                        getMvpView().handleUserInfo(User.getUser(wbUserInfo));
                    }
                });
    }

    public Map<String, String> getWBTopicParameter(String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getToken());
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }
        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return ub.getParameters();
    }

    public Subscriber<WBTopics> getWBTopicsSubscriber(final boolean isRefresh) {
        return new Subscriber<WBTopics>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().stopLoading(isRefresh);
            }

            @Override
            public void onNext(WBTopics wbTopics) {
                getMvpView().stopLoading(isRefresh);
                getMvpView().handleTopicData(Topic.getTopics(wbTopics), isRefresh);
            }
        };
    }
}
