package com.hengye.share.module.topic;

import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.BuildConfig;
import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicUrl;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBShortUrl;
import com.hengye.share.model.sina.WBShortUrls;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicIds;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.WBUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import static com.hengye.share.util.DataUtil.WEB_URL;

public class TopicPresenter extends ListDataPresenter<Topic, TopicMvpView> {

    private TopicGroup mTopicGroup;
    private String uid, name;

    public TopicPresenter(TopicMvpView mvpView, TopicGroup topicGroup) {
        super(mvpView);
        mTopicGroup = topicGroup;
    }

    public void loadWBTopic(String id, final boolean isRefresh) {
        loadWBTopic(id, isRefresh, true);
    }

    public void loadWBTopic(String id, final boolean isRefresh, boolean isLoadId) {
        getTopics(id, isRefresh, isLoadId)
                .flatMap(TopicRxUtil.flatShortUrl())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getTopicsSubscriber(isRefresh));
    }

    private Observable<WBTopicIds> getWBTopicIds(String id) {
        switch (mTopicGroup.topicType) {
            case ALL:
            default:
                return RetrofitManager
                        .getWBService()
                        .listTopicIds(getWBAllTopicParameter(id, true));
            case BILATERAL:
                return RetrofitManager
                        .getWBService()
                        .listBilateralTopicIds(getWBAllTopicParameter(id, true));
            case GROUP_LIST:
                return RetrofitManager
                        .getWBService()
                        .listGroupTopicIds(getWBAllTopicParameter(id, true));
        }
    }

    private Observable<WBTopics> getWBTopics(String id, final boolean isRefresh) {
        switch (mTopicGroup.topicType) {
            case ALL:
            default:
                return RetrofitManager
                        .getWBService()
                        .listTopic(getWBAllTopicParameter(id, isRefresh));
            case BILATERAL:
                return RetrofitManager
                        .getWBService()
                        .listBilateralTopic(getWBAllTopicParameter(id, isRefresh));
            case GROUP_LIST:
                return RetrofitManager
                        .getWBService()
                        .listGroupTopic(getWBAllTopicParameter(id, isRefresh));
        }
    }

    private Observable<ArrayList<Topic>> getTopics(String id, final boolean isRefresh, boolean isLoadId) {
        switch (mTopicGroup.topicType) {
            case ALL:
            case BILATERAL:
            case GROUP_LIST:
            default:

                boolean isNeedLoadId = false;
                if (isRefresh && isLoadId && !"0".equals(id)) {
                    isNeedLoadId = true;
                }
//                if (!SettingHelper.isOrderReading()) {
//                    isNeedLoadId = false;
//                }
                if (isRefresh && !isNeedLoadId) {
                    if (mTopicGroup.topicType == TopicType.GROUP_LIST) {
                        id = "1";
                    } else {
                        id = "0";
                    }
                }

                if (isRefresh && BuildConfig.DEBUG) {
                    isNeedLoadId = false;
                    if (mTopicGroup.topicType == TopicType.GROUP_LIST) {
                        id = "1";
                    } else {
                        id = "0";
                    }
                }

                if (isNeedLoadId) {
                    return getWBTopicIds(id)
                            .flatMap(flatWBTopicId(id))
                            .flatMap(flatWBTopics());
                } else {
                    return getWBTopics(id, isRefresh)
                            .flatMap(flatWBTopics());
                }
            case TOPIC_AT_ME:
                return RetrofitManager
                        .getWBService()
                        .listTopicAtMeTopic(getWBAllTopicParameter(id, isRefresh))
                        .flatMap(flatWBTopics());
            case COMMENT_AT_ME:
                return RetrofitManager
                        .getWBService()
                        .listCommentAtMeTopic(getWBAllTopicParameter(id, isRefresh))
                        .flatMap(flatWBTopicComments());
            case COMMENT_TO_ME:
                return RetrofitManager
                        .getWBService()
                        .listCommentToMeTopic(getWBAllTopicParameter(id, isRefresh))
                        .flatMap(flatWBTopicComments());
            case COMMENT_BY_ME:
                return RetrofitManager
                        .getWBService()
                        .listCommentByMeTopic(getWBAllTopicParameter(id, isRefresh))
                        .flatMap(flatWBTopicComments());
            case HOMEPAGE:
                Map<String, String> params = getWBAllTopicParameter(id, isRefresh);
                params.put("access_token", UserUtil.getPriorToken());
                return RetrofitManager
                        .getWBService()
                        .listUserTopic(params)
                        .flatMap(flatWBTopics());
        }
    }

    public Map<String, String> getWBAllTopicParameter(String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getToken());
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }
        if (!TextUtils.isEmpty(uid)) {
            ub.addParameter("uid", uid);
        } else if (!TextUtils.isEmpty(name)) {
            ub.addParameter("screen_name", name);
        }
        if (mTopicGroup.groupList != null) {
            ub.addParameter("list_id", mTopicGroup.groupList.getGid());
        }

        ub.addParameter("count", WBUtil.getWBTopicRequestCount());
        return ub.getParameters();
    }

    private Subscriber<List<Topic>> getTopicsSubscriber(final boolean isRefresh) {
        return new ListDataSubscriber(isRefresh);
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

    Func1<WBTopicComments, Observable<ArrayList<Topic>>> mFlatWBTopicComments;

    private Func1<WBTopicComments, Observable<ArrayList<Topic>>> flatWBTopicComments() {
        if (mFlatWBTopicComments == null) {
            mFlatWBTopicComments = new Func1<WBTopicComments, Observable<ArrayList<Topic>>>() {
                @Override
                public Observable<ArrayList<Topic>> call(WBTopicComments wbComments) {
                    return Observable.just(Topic.getTopics(wbComments));
                }
            };
        }
        return mFlatWBTopicComments;
    }

    private Func1<WBTopicIds, Observable<WBTopics>> flatWBTopicId(final String since_id) {
        return new Func1<WBTopicIds, Observable<WBTopics>>() {
            @Override
            public Observable<WBTopics> call(WBTopicIds wbTopicIds) {

                Observable<WBTopics> observable;
                if (wbTopicIds == null || CommonUtil.isEmpty(wbTopicIds.getStatuses())) {
                    //没有新的微博
                    L.debug("no topic update");
                    observable = Observable.empty();
                } else {
                    if (wbTopicIds.getStatuses().size() >= WBUtil.getWBTopicRequestCount()) {
                        //还有更新的微博，重新请求刷新
//                                RequestManager.addToRequestQueue(getWBTopicRequest(token, 0 + "", true), getRequestTag());
                        L.debug("exist newer topic, request refresh again");
                        observable = getWBTopics("0", true);
                    } else {
                        //新的微博条数没有超过请求条数，显示更新多少条微博，根据请求的since_id获取微博
                        L.debug("new topic is less than MAX_COUNT_REQUEST, request refresh by since_id");
                        observable = getWBTopics(since_id, true);
                    }
                }

                return observable;
            }
        };
    }

    /**
     * 先检测有没有缓存，没有再请求服务器
     */
    public void loadWBTopic(final String firstPage) {
        getMvpView().onTaskStart();
        Observable
                .create(new Observable.OnSubscribe<ArrayList<Topic>>() {
                    @Override
                    public void call(Subscriber<? super ArrayList<Topic>> subscriber) {
                        subscriber.onNext(findData());
                        subscriber.onCompleted();
                    }
                })
                .flatMap(new Func1<ArrayList<Topic>, Observable<ArrayList<Topic>>>() {
                    @Override
                    public Observable<ArrayList<Topic>> call(ArrayList<Topic> topics) {
                        if (CommonUtil.isEmpty(topics)) {
                            return getTopics(firstPage, true, false);
                        } else {
                            return Observable
                                    .just(topics)
                                    .delay(400, TimeUnit.MILLISECONDS);
                        }
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getTopicsSubscriber(true));
    }

    public ArrayList<Topic> findData() {
        return ShareJson.findData(getModelName(), new TypeToken<ArrayList<Topic>>() {
        }.getType());
    }

    public void saveData(List<Topic> data) {
        ShareJson.saveListData(getModelName(), data);
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
        ALL, BILATERAL, COMMENT_AT_ME, TOPIC_AT_ME, COMMENT_TO_ME, COMMENT_BY_ME, HOMEPAGE, GROUP_LIST, NONE
    }

    public static class TopicGroup implements Serializable {

        private static final long serialVersionUID = -5394322343417513819L;

        private TopicType topicType;

        public TopicGroup(TopicType topicType) {
            this.topicType = topicType;
        }

        public TopicGroup(TopicType topicType, GroupList groupList) {
            this.topicType = topicType;
            this.groupList = groupList;
        }

        private GroupList groupList;

        public GroupList getGroupList() {
            return groupList;
        }

        public void setGroupList(GroupList groupList) {
            this.groupList = groupList;
        }

        public TopicType getTopicType() {
            return topicType;
        }

        public void setTopicType(TopicType topicType) {
            this.topicType = topicType;
        }

        /**
         * @return 如果是首页的列表内容, 则返回true;
         */
        public boolean isReadingType() {
            return topicType == TopicType.ALL || topicType == TopicType.BILATERAL || topicType == TopicType.GROUP_LIST;
        }

        @Override
        public String toString() {
            if (groupList == null) {
                return topicType.toString();
            }

            return topicType
                    + "/"
                    + groupList.getGid();
        }

        public String getName() {
            return getName(this, ResUtil.getResources());
        }

        public static List<TopicGroup> getAllTopicGroups() {
            ArrayList<TopicGroup> topicGroupGroups = new ArrayList<>();
            topicGroupGroups.add(new TopicGroup(TopicType.ALL));
            topicGroupGroups.add(new TopicGroup(TopicType.BILATERAL));
            List<TopicGroup> temp = TopicGroup.getTopicGroups();
            if (!CommonUtil.isEmpty(temp)) {
                topicGroupGroups.addAll(temp);
            }

            return topicGroupGroups;
        }

        public static List<TopicGroup> getTopicGroups() {
            return getTopicGroups(UserUtil.queryGroupList());
        }

        public static List<TopicGroup> getTopicGroups(List<GroupList> groupLists) {
            if (CommonUtil.isEmpty(groupLists)) {
                return null;
            }
            List<TopicGroup> result = new ArrayList<>();
            for (GroupList gl : groupLists) {
                result.add(new TopicGroup(TopicType.GROUP_LIST, gl));
            }
            return result;
        }

        /**
         * @param groupIdStr
         * @return 返回与@param groupIdStr 对应的分组名字
         */
        public static String getTopicGroupName(String groupIdStr) {
            List<TopicPresenter.TopicGroup> topicGroups = TopicPresenter.TopicGroup.getTopicGroups();
            if (!CommonUtil.isEmpty(groupIdStr) && !CommonUtil.isEmpty(topicGroups)) {
                for (TopicPresenter.TopicGroup tg : topicGroups) {
                    if (groupIdStr.equals(tg.getGroupList().getGid())) {
                        return tg.getName();
                    }
                }
            }
            return null;
        }

        private static String getName(TopicGroup topicGroup, Resources resources) {
            switch (topicGroup.topicType) {
                case ALL:
                    return resources.getString(R.string.title_page_all);
                case BILATERAL:
                    return resources.getString(R.string.title_page_bilateral);
                case COMMENT_AT_ME:
                    return resources.getString(R.string.title_page_comment);
                case COMMENT_TO_ME:
                    return resources.getString(R.string.title_page_comment_to_me);
                case COMMENT_BY_ME:
                    return resources.getString(R.string.title_page_comment_by_me);
                case TOPIC_AT_ME:
                    return resources.getString(R.string.title_page_topic);
                case GROUP_LIST:
                    if (topicGroup.groupList == null) {
                        return "未知分组";
                    } else {
                        return topicGroup.groupList.getName();
                    }
                case NONE:
                    return resources.getString(R.string.title_activity_group_manage);
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


}
