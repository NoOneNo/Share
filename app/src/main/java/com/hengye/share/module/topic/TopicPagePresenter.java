package com.hengye.share.module.topic;

import android.content.res.Resources;
import android.text.TextUtils;

import com.hengye.share.R;
import com.hengye.share.model.TopicFavorites;
import com.hengye.share.model.sina.WBTopicFavorites;
import com.hengye.share.util.handler.TopicNumberPager;
import com.hengye.share.model.Topic;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class TopicPagePresenter extends RxPresenter<TopicPageMvpView> {

    private TopicGroup mTopicGroup;
    private String mKeyword;
    private String uid, name;
    private TopicNumberPager mPager;

    public TopicPagePresenter(TopicPageMvpView mvpView, TopicGroup topicGroup, TopicNumberPager pager) {
        super(mvpView);
        mTopicGroup = topicGroup;
        mPager = pager;
    }

    public void loadWBTopic(boolean isRefresh) {
        getTopics(isRefresh)
                .flatMap(TopicRxUtil.flatShortUrl())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getTopicsSubscriber(isRefresh));
    }

    private Observable<ArrayList<Topic>> getTopics(final boolean isRefresh) {
        switch (mTopicGroup.topicType) {
            case THEME:
                return RetrofitManager
                        .getWBService()
                        .searchTopic(getWBAllTopicParameter(mPager.getPage(isRefresh)))
                        .flatMap(flatWBTopics());
            case FAVORITES:
            default:
                return RetrofitManager
                        .getWBService()
                        .listFavoritesTopic(getWBAllTopicParameter(mPager.getPage(isRefresh)))
                        .flatMap(flatWBTopicFavorites());
        }
    }

    private Map<String, String> getWBAllTopicParameter(int page) {
        final UrlBuilder ub = new UrlBuilder();
//        ub.addParameter("access_token", "2.00tJ6X3GiGSdcC5f7433b5a40iAPJB");
        ub.addParameter("access_token", UserUtil.getPriorToken());

        ub.addParameter("page", page);

        if (!TextUtils.isEmpty(getKeyword())) {
            ub.addParameter("q", getKeyword());
        }

//        if (!TextUtils.isEmpty(uid)) {
//            ub.addParameter("uid", uid);
//        } else if (!TextUtils.isEmpty(name)) {
//            ub.addParameter("screen_name", name);
//        }

        ub.addParameter("count", mPager.getPageSize());
        return ub.getParameters();
    }

    private Subscriber<ArrayList<Topic>> getTopicsSubscriber(final boolean isRefresh) {
        return new BaseSubscriber<ArrayList<Topic>>() {
            @Override
            public void onError(TopicPageMvpView v, Throwable e) {
                v.onTaskComplete(isRefresh, false);
            }

            @Override
            public void onNext(TopicPageMvpView v, ArrayList<Topic> topics) {
                v.onTaskComplete(isRefresh, true);
                v.handleTopicData(topics, isRefresh);
            }
        };
    }

    Func1<WBTopics, Observable<ArrayList<Topic>>> mFlatWBTopics;
    private Func1<WBTopics, Observable<ArrayList<Topic>>> flatWBTopics() {
        if (mFlatWBTopics == null) {
            mFlatWBTopics = new Func1<WBTopics, Observable<ArrayList<Topic>>>() {
                @Override
                public Observable<ArrayList<Topic>> call(WBTopics wbTopics) {
                    return Observable.just(Topic.getTopics(wbTopics));
                }
            };
        }
        return mFlatWBTopics;
    }

    Func1<WBTopicFavorites, Observable<ArrayList<Topic>>> mFlatWBTopicFavorites;
    private Func1<WBTopicFavorites, Observable<ArrayList<Topic>>> flatWBTopicFavorites() {
        if (mFlatWBTopicFavorites == null) {
            mFlatWBTopicFavorites = new Func1<WBTopicFavorites, Observable<ArrayList<Topic>>>() {
                @Override
                public Observable<ArrayList<Topic>> call(WBTopicFavorites wbTopicFavorites) {
                    return Observable.just(TopicFavorites.getTopics(wbTopicFavorites));
                }
            };
        }
        return mFlatWBTopicFavorites;
    }

    public enum TopicType {
        THEME, FAVORITES
    }

    public static class TopicGroup implements Serializable {

        private static final long serialVersionUID = -5809183018190271012L;

        private TopicType topicType;

        public TopicGroup(TopicType topicType) {
            this.topicType = topicType;
        }

        public TopicType getTopicType() {
            return topicType;
        }

        public void setTopicType(TopicType topicType) {
            this.topicType = topicType;
        }

        @Override
        public String toString() {
            return topicType.toString();
        }

        public static String getName(TopicGroup topicGroup, Resources resources) {
            switch (topicGroup.topicType) {
                case THEME:
                    return resources.getString(R.string.title_page_theme);
                case FAVORITES:
                    return resources.getString(R.string.title_page_favorites);
                default:
                    return null;
            }
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return mKeyword;
    }

    public void setKeyword(String keyword) {
        this.mKeyword = keyword;
    }
}
