package com.hengye.share.ui.presenter;

import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.R;
import com.hengye.share.handler.data.NumberPager;
import com.hengye.share.handler.data.base.Pager;
import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.GreenDaoManager;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicFavorites;
import com.hengye.share.model.sina.WBTopicIds;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.ui.mvpview.TopicMvpView;
import com.hengye.share.ui.mvpview.TopicPageMvpView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.thirdparty.WBUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TopicPagePresenter extends BasePresenter<TopicPageMvpView> {

    private TopicGroup mTopicGroup;
    private String mKeyword;
    private String uid, name;
    private NumberPager mPager;

    public TopicPagePresenter(TopicPageMvpView mvpView, TopicGroup topicGroup, NumberPager pager) {
        super(mvpView);
        mTopicGroup = topicGroup;
        mPager = pager;
    }

    public void loadWBTopic(boolean isRefresh) {
        switch (mTopicGroup.topicType) {
            case THEME:
                loadWBThemeTopic(isRefresh);
                break;
            case FAVORITES:
                loadWBFavoritesTopic(isRefresh);
                break;
            default:
                break;
        }

    }

    public void loadWBThemeTopic(boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .searchTopic(getWBAllTopicParameter(mPager.getPage(isRefresh)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicsSubscriber(isRefresh));
    }

    public void loadWBFavoritesTopic(boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listFavoritesTopic(getWBAllTopicParameter(mPager.getPage(isRefresh)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicsSubscriber(isRefresh));
    }

    public Map<String, String> getWBAllTopicParameter(int page) {
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

        ub.addParameter("count", WBUtil.getWBTopicRequestCount());
        return ub.getParameters();
    }

    public Subscriber<WBTopics> getWBTopicsSubscriber(final boolean isRefresh) {
        return new BaseSubscriber<WBTopics>() {
            @Override
            public void handleViewOnFail(TopicPageMvpView v, Throwable e) {
                v.stopLoading(isRefresh);
            }

            @Override
            public void handleViewOnSuccess(TopicPageMvpView v, WBTopics wbTopics) {
                v.stopLoading(isRefresh);
                v.handleTopicData(Topic.getTopics(wbTopics), isRefresh);
            }
        };
    }

//    public Subscriber<WBTopicFavorites> getWBFavoritesTopicsSubscriber(final boolean isRefresh) {
//        return new BaseSubscriber<WBTopicFavorites>() {
//            @Override
//            public void handleViewOnFail(TopicPageMvpView v, Throwable e) {
//                v.stopLoading(isRefresh);
//            }
//
//            @Override
//            public void handleViewOnSuccess(TopicPageMvpView v, WBTopicFavorites wbTopics) {
//                v.stopLoading(isRefresh);
//                v.handleTopicData(Topic.get(wbTopics), isRefresh);
//            }
//        };
//    }

    public enum TopicType {
        THEME, FAVORITES;
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
