package com.hengye.share.module.topic;

import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicFavorites;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBTopicFavorites;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.handler.TopicNumberPager;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

public class TopicPagePresenter extends ListDataPresenter<Topic, TopicPageMvpView> {

    private TopicGroup mTopicGroup;
    private String mKeyword;
    private String uid, name;
    private TopicNumberPager mPager;

    public TopicPagePresenter(TopicPageMvpView mvpView, TopicGroup topicGroup, TopicNumberPager pager) {
        super(mvpView);
        mTopicGroup = topicGroup;
        mPager = pager;
    }

    public void loadRemoteWBTopic(boolean isRefresh) {
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

    private Observer<List<Topic>> getTopicsSubscriber(final boolean isRefresh) {
        return new ListDataSubscriberList(isRefresh);
    }

    Function<WBTopics, Observable<ArrayList<Topic>>> mFlatWBTopics;

    private Function<WBTopics, Observable<ArrayList<Topic>>> flatWBTopics() {
        if (mFlatWBTopics == null) {
            mFlatWBTopics = new Function<WBTopics, Observable<ArrayList<Topic>>>() {
                @Override
                public Observable<ArrayList<Topic>> apply(WBTopics wbTopics) {
                    return ObservableHelper.justArrayList(Topic.getTopics(wbTopics));
                }
            };
        }
        return mFlatWBTopics;
    }

    Function<WBTopicFavorites, Observable<ArrayList<Topic>>> mFlatWBTopicFavorites;

    private Function<WBTopicFavorites, Observable<ArrayList<Topic>>> flatWBTopicFavorites() {
        if (mFlatWBTopicFavorites == null) {
            mFlatWBTopicFavorites = new Function<WBTopicFavorites, Observable<ArrayList<Topic>>>() {
                @Override
                public Observable<ArrayList<Topic>> apply(WBTopicFavorites wbTopicFavorites) {
                    return ObservableHelper.justArrayList(TopicFavorites.getTopics(wbTopicFavorites));
                }
            };
        }
        return mFlatWBTopicFavorites;
    }

    /**
     * 先检测有没有缓存，没有再请求服务器
     */
    public void loadWBTopic() {
        getMvpView().onTaskStart();


        ObservableHelper.justArrayList(findData())
                .flatMap(new Function<ArrayList<Topic>, Observable<ArrayList<Topic>>>() {
                    @Override
                    public Observable<ArrayList<Topic>> apply(ArrayList<Topic> topics) {
                        if (CommonUtil.isEmpty(topics)) {
                            return getTopics(true);
                        } else {
                            return Observable
                                    .just(topics)
                                    .delay(400, TimeUnit.MILLISECONDS);
                        }
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribeWith(getTopicsSubscriber(true));
    }

    public ArrayList<Topic> findData() {
        if (mTopicGroup.getTopicType() == TopicType.FAVORITES) {
            return ShareJson.findData(getModelName(), new TypeToken<ArrayList<Topic>>() {
            }.getType());
        }else{
            return null;
        }
    }

    public void saveData(List<Topic> data) {
        if(isNeedCache()) {
            ShareJson.saveListData(getModelName(), data);
        }
    }

    public boolean isNeedCache() {
        if (mTopicGroup.topicType == TopicType.THEME) {
            return false;
        }
        return true;
    }

    private String mModuleName;
    private String mModuleUid;

    public String getModelName() {
        if (mModuleName == null || mModuleUid == null || !mModuleUid.equals(UserUtil.getUid())) {
            mModuleUid = UserUtil.getUid();
            mModuleName = Topic.class.getSimpleName()
                    + mModuleUid
                    + "/"
                    + uid
                    + "/"
                    + name
                    + "/"
                    + mTopicGroup;
        }
        return mModuleName;
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
