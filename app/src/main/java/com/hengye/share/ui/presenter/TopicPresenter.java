package com.hengye.share.ui.presenter;

import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.GreenDaoManager;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicIds;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.ui.mvpview.TopicMvpView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.L;
import com.hengye.share.helper.SettingHelper;
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

public class TopicPresenter extends BasePresenter<TopicMvpView> {

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
        switch (mTopicGroup.topicType) {
            case ALL:
            case BILATERAL:
            case GROUP_LIST:

                boolean isNeedLoadId = false;
                if (isRefresh && isLoadId && !"0".equals(id)) {
                    isNeedLoadId = true;
                }
                if(!SettingHelper.isOrderReading()){
                    isNeedLoadId = false;
                }
                loadWBTopicIds(id, isRefresh, isNeedLoadId);

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
            default:
                break;
        }
    }

    public void loadWBTopicIds(String id, boolean isRefresh, boolean isNeedLoadId){
        switch (mTopicGroup.topicType){
            case ALL:
                if(isNeedLoadId){
                    loadWBAllTopicIds(id);
                }else{
                    loadWBAllTopic(id, isRefresh);
                }
                break;
            case BILATERAL:
                if(isNeedLoadId){
                    loadWBBilateralTopicIds(id);
                }else{
                    loadWBBilateralTopic(id, isRefresh);
                }
                break;
            case GROUP_LIST:
                if(isNeedLoadId){
                    loadWBGroupListTopicIds(id);
                }else{
                    loadWBGroupListTopic(id, isRefresh);
                }
                break;
        }
    }

    public void loadWBAllTopicIds(final String since_id) {
        RetrofitManager
                .getWBService()
                .listTopicIds(getWBAllTopicParameter(since_id, true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicIdsSubscriber(since_id));

    }

    public void loadWBAllTopic(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listTopic(getWBAllTopicParameter(id, isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicsSubscriber(isRefresh));
    }

    public void loadWBBilateralTopicIds(final String since_id) {
        RetrofitManager
                .getWBService()
                .listBilateralTopicIds(getWBAllTopicParameter(since_id, true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicIdsSubscriber(since_id));

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
        Map<String, String> params = getWBAllTopicParameter(id, isRefresh);
        params.put("access_token", UserUtil.getPriorToken());
        RetrofitManager
                .getWBService()
                .listUserTopic(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicsSubscriber(isRefresh));
    }

    public void loadWBGroupListTopicIds(final String since_id) {
        RetrofitManager
                .getWBService()
                .listGroupTopicIds(getWBAllTopicParameter(since_id, true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWBTopicIdsSubscriber(since_id));
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

        ub.addParameter("count", WBUtil.getWBTopicRequestCount());
        return ub.getParameters();
    }

    public Subscriber<WBTopicIds> getWBTopicIdsSubscriber(final String since_id) {
        return new BaseSubscriber<WBTopicIds>() {
            @Override
            public void handleViewOnFail(TopicMvpView v, Throwable e) {
                v.stopLoading(true);
            }

            @Override
            public void handleViewOnSuccess(TopicMvpView v, WBTopicIds wbTopicIds) {
                if (wbTopicIds == null || CommonUtil.isEmpty(wbTopicIds.getStatuses())) {
                    //没有新的微博
                    getMvpView().stopLoading(true);
                    getMvpView().handleNoMoreTopics();
                    L.debug("no topic update");
                } else {
                    if (wbTopicIds.getStatuses().size() >= WBUtil.getWBTopicRequestCount() * 2) {
                        //还有更新的微博，重新请求刷新
//                                RequestManager.addToRequestQueue(getWBTopicRequest(token, 0 + "", true), getRequestTag());
                        L.debug("exist newer topic, request refresh again");
                        loadWBTopic("0", true, false);
                    } else {
                        //新的微博条数没有超过请求条数的2倍，显示更新多少条微博，根据请求的since_id获取微博
//                                RequestManager.addToRequestQueue(getWBTopicRequest(token, since_id, true), getRequestTag());
                        L.debug("new topic is less than MAX_COUNT_REQUEST, request refresh by since_id");
                        loadWBTopic(since_id, true, false);
                    }
                }
            }
        };
    }


    public Subscriber<WBTopics> getWBTopicsSubscriber(final boolean isRefresh) {
        return new BaseSubscriber<WBTopics>() {
            @Override
            public void handleViewOnFail(TopicMvpView v, Throwable e) {
                v.stopLoading(isRefresh);
            }

            @Override
            public void handleViewOnSuccess(TopicMvpView v, WBTopics wbTopics) {
                v.stopLoading(isRefresh);
                v.handleTopicData(Topic.getTopics(wbTopics), isRefresh);
            }
        };
    }

    public Subscriber<WBTopicComments> getWBCommentsSubscriber(final boolean isRefresh) {
        return new BaseSubscriber<WBTopicComments>() {
            @Override
            public void handleViewOnFail(TopicMvpView v, Throwable e) {
                v.stopLoading(isRefresh);
            }

            @Override
            public void handleViewOnSuccess(TopicMvpView v, WBTopicComments wbTopicComments) {
                v.stopLoading(isRefresh);
                v.handleTopicData(Topic.getTopics(wbTopicComments), isRefresh);
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
                .subscribe(new BaseAction1<ArrayList<Topic>>() {
                    @Override
                    public void handleView(TopicMvpView v, ArrayList<Topic> topics) {
                        v.handleCache(topics);
                    }
                });
    }

    public ArrayList<Topic> findData() {

        ShareJson shareJson = null;

        try{
            shareJson = GreenDaoManager.getDaoSession().getShareJsonDao().load(getModuleName());
        }catch (Exception e){
            e.printStackTrace();
        }

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
    private String mModuleUid;

    public String getModuleName() {
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

        public boolean isReadingType(){
            if(topicType == TopicType.ALL || topicType == TopicType.BILATERAL || topicType == TopicType.GROUP_LIST){
                return true;
            }
            return false;
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

        public static List<TopicGroup> getTopicGroup() {
            return getTopicGroup(UserUtil.queryGroupList());
        }

        public static List<TopicGroup> getTopicGroup(List<GroupList> groupLists) {
            if (CommonUtil.isEmpty(groupLists)) {
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
