package com.hengye.share.ui.presenter;

import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.GreenDaoManager;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.greenrobot.ShareJsonDao;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicIds;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.ui.mvpview.TopicMvpView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;
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
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TopicPresenter extends BasePresenter<TopicMvpView> {

    private TopicGroup mTopicGroup;
    private String uid, name;

    public TopicPresenter(TopicMvpView mvpView, TopicGroup topicGroup) {
        super(mvpView);
        mTopicGroup = topicGroup;
    }

    public void loadWBAllTopic(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listTopic(getWBAllTopicParameter(id, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicsSubscriber(isRefresh));
    }

    public void loadWBTopic(String id, final boolean isRefresh) {
        switch (mTopicGroup.topicType) {
            case ALL:
                if (isRefresh && !"0".equals(id)) {
                    loadWBAllTopicIds(id);
                } else {
                    loadWBAllTopic(id, isRefresh);
                }
                break;
            case BILATERAL:
                loadWBBilateralTopic(id, isRefresh);
                break;
            case TOPIC_AT_ME:
                loadWBTopicAtMeTopic(id, isRefresh);
                break;
            case COMMENT_AT_ME:
                loadWBCommentAtMeTopic(id, isRefresh);
                break;
            case COMMENT_TO_ME:
                loadWBCommentToMeTopic(id, isRefresh);
                break;
            case COMMENT_BY_ME:
                loadWBCommentByMeTopic(id, isRefresh);
                break;
            case HOMEPAGE:
                loadWBHomepageTopic(id, isRefresh);
                break;
            case GROUP_LIST:
                loadWBGroupListTopic(id, isRefresh);
                break;
            default:
                break;
        }


    }

    public void loadWBAllTopicIds(final String since_id) {
        RetrofitManager
                .getWBService()
                .listTopicIds(getWBAllTopicParameter(since_id, true))
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
                                loadWBAllTopic("0", true);
                            } else {
                                //新的微博条数没有超过请求条数，显示更新多少条微博，根据请求的since_id获取微博
//                                RequestManager.addToRequestQueue(getWBTopicRequest(token, since_id, true), getRequestTag());
                                L.debug("new topic is less than MAX_COUNT_REQUEST, request refresh by since_id");
                                loadWBAllTopic(since_id, true);
                            }
                        }
                    }
                });

    }

    public void loadWBBilateralTopic(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listBilateralTopic(getWBAllTopicParameter(id, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicsSubscriber(isRefresh));
    }

    public void loadWBCommentAtMeTopic(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listCommentAtMeTopic(getWBAllTopicParameter(id, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBCommentsSubscriber(isRefresh));
    }

    public void loadWBTopicAtMeTopic(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listTopicAtMeTopic(getWBAllTopicParameter(id, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicsSubscriber(isRefresh));
    }

    public void loadWBCommentToMeTopic(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listCommentToMeTopic(getWBAllTopicParameter(id, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBCommentsSubscriber(isRefresh));
    }

    public void loadWBCommentByMeTopic(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listCommentByMeTopic(getWBAllTopicParameter(id, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBCommentsSubscriber(isRefresh));
    }

    public void loadWBHomepageTopic(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listUserTopic(getWBAllTopicParameter(id, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicsSubscriber(isRefresh));
    }

    public void loadWBGroupListTopic(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listGroupTopic(getWBAllTopicParameter(id, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicsSubscriber(isRefresh));
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

    public Subscriber<WBTopicComments> getWBCommentsSubscriber(final boolean isRefresh) {
        return new Subscriber<WBTopicComments>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                getMvpView().stopLoading(isRefresh);
            }

            @Override
            public void onNext(WBTopicComments wbTopicComments) {
                getMvpView().stopLoading(isRefresh);
                getMvpView().handleTopicData(Topic.getTopics(wbTopicComments), isRefresh);
            }
        };
    }

    public void loadCacheData(){
        Observable
                .create(new Observable.OnSubscribe<ArrayList<Topic>>() {
                    @Override
                    public void call(Subscriber<? super ArrayList<Topic>> subscriber) {
                        subscriber.onNext(findData());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ArrayList<Topic>>() {
                    @Override
                    public void call(ArrayList<Topic> data) {
                        getMvpView().handleCache(data);
                    }
                });
    }

    public ArrayList<Topic> findData() {

        ShareJson shareJson = GreenDaoManager.getDaoSession().getShareJsonDao().load(getModuleName());

        if (shareJson == null) {
            return new ArrayList<>();
        }

        ArrayList<Topic> data = GsonUtil.fromJson(shareJson.getJson()
                , new TypeToken<ArrayList<Topic>>(){}.getType());

        return data == null ? new ArrayList<Topic>() : data;
    }

    public void saveData(List<Topic> data) {
        GreenDaoManager
                .getDaoSession()
                .getShareJsonDao()
                .insertOrReplace(new ShareJson(getModuleName(), GsonUtil.toJson(data)));
    }

    private String mModuleName;

    public String getModuleName() {
        if (mModuleName == null) {
            mModuleName = Topic.class.getSimpleName()
                    + UserUtil.getUid()
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
        ALL, BILATERAL, COMMENT_AT_ME, TOPIC_AT_ME, COMMENT_TO_ME, COMMENT_BY_ME, HOMEPAGE, GROUP_LIST
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

        @Override
        public String toString() {
            if (groupList == null) {
                return topicType.toString();
            }

            return topicType
                    + "/"
                    + groupList.getGid();
        }

        public static List<TopicGroup> getTopicGroup() {
            return getTopicGroup(UserUtil.queryGroupList());
        }

        public static List<TopicGroup> getTopicGroup(List<GroupList> groupLists) {
            if (CommonUtil.isEmptyCollection(groupLists)) {
                return null;
            }
            List<TopicGroup> result = new ArrayList<>();
            for (GroupList gl : groupLists) {
                result.add(new TopicGroup(TopicType.GROUP_LIST, gl));
            }
            return result;
        }

        public static String getName(TopicGroup topicGroup, Resources resources) {
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
