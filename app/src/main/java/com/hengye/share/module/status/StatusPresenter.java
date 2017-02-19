package com.hengye.share.module.status;

import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBStatus;
import com.hengye.share.model.sina.WBStatusIds;
import com.hengye.share.model.sina.WBStatuses;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.WBUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

import static com.hengye.share.module.status.StatusRxUtil.flatWBStatusComments;
import static com.hengye.share.module.status.StatusRxUtil.flatWBStatuses;

public class StatusPresenter extends ListDataPresenter<Status, StatusContract.View> {

    private StatusGroup mStatusGroup;
    private String uid, name;

    public StatusPresenter(StatusContract.View mvpView, StatusGroup statusGroup) {
        super(mvpView);
        mStatusGroup = statusGroup;
    }

    public void loadWBStatus(String id, final boolean isRefresh) {
        loadWBStatus(id, isRefresh, true);
    }

    public void loadWBStatus(String id, final boolean isRefresh, boolean isLoadId) {
        getMvpView().onTaskStart();
        getStatuses(id, isRefresh, isLoadId)
                .flatMap(StatusRxUtil.flatStatusShortUrl())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribeWith(getStatusesSubscriber(isRefresh));
    }

    private Single<WBStatusIds> getWBStatusIds(String id) {
        switch (mStatusGroup.statusType) {
            case ALL:
            default:
                return RetrofitManager
                        .getWBService()
                        .listStatusIds(getWBAllStatusParameter(id, true));
            case BILATERAL:
                return RetrofitManager
                        .getWBService()
                        .listBilateralStatusIds(getWBAllStatusParameter(id, true));
            case GROUP_LIST:
                return RetrofitManager
                        .getWBService()
                        .listGroupStatusIds(getWBAllStatusParameter(id, true));
        }
    }

    private Single<WBStatuses> getWBStatuses(String id, final boolean isRefresh) {
        switch (mStatusGroup.statusType) {
            case ALL:
            default:
                return RetrofitManager
                        .getWBService()
                        .listStatus(getWBAllStatusParameter(id, isRefresh));
            case BILATERAL:
                return RetrofitManager
                        .getWBService()
                        .listBilateralStatus(getWBAllStatusParameter(id, isRefresh));
            case GROUP_LIST:
                return RetrofitManager
                        .getWBService()
                        .listGroupStatus(getWBAllStatusParameter(id, isRefresh));
        }
    }

    private Single<ArrayList<Status>> getStatuses(String id, final boolean isRefresh, boolean isLoadId) {
        switch (mStatusGroup.statusType) {
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
                    if (mStatusGroup.statusType == StatusType.GROUP_LIST) {
                        id = "1";
                    } else {
                        id = "0";
                    }
                }

                //暂时先统一不设置loadId
//                if (isRefresh && BuildConfig.DEBUG) {
                if (isRefresh) {
                    isNeedLoadId = false;
                    if (mStatusGroup.statusType == StatusType.GROUP_LIST) {
                        id = "1";
                    } else {
                        id = "0";
                    }
                }

                if (isNeedLoadId) {
                    return getWBStatusIds(id)
                            .flatMap(flatWBStatusId(id))
                            .flatMap(flatWBStatuses());
                } else {
                    return getWBStatuses(id, isRefresh)
                            .flatMap(flatWBStatuses());
                }
            case TOPIC_AT_ME:
                return RetrofitManager
                        .getWBService()
                        .listAtMeStatus(getWBAllStatusParameter(id, isRefresh))
                        .flatMap(flatWBStatuses());
            case COMMENT_AT_ME:
                return RetrofitManager
                        .getWBService()
                        .listCommentAtMeStatus(getWBAllStatusParameter(id, isRefresh))
                        .flatMap(flatWBStatusComments());
            case COMMENT_TO_ME:
                return RetrofitManager
                        .getWBService()
                        .listCommentToMeStatus(getWBAllStatusParameter(id, isRefresh))
                        .flatMap(flatWBStatusComments());
            case COMMENT_BY_ME:
                return RetrofitManager
                        .getWBService()
                        .listCommentByMeStatus(getWBAllStatusParameter(id, isRefresh))
                        .flatMap(flatWBStatusComments());
            case HOMEPAGE:
                if(isRefresh){
                    id = "0";
                }
                Map<String, String> params = getWBAllStatusParameter(id, isRefresh);
                String token;
                if(uid != null && uid.equals(UserUtil.getUid())){
                    token = UserUtil.getToken();
                }else{
                    token = UserUtil.getPriorToken();
                }
                params.put("access_token", token);
                return RetrofitManager
                        .getWBService()
                        .listUserStatus(params)
                        .flatMap(flatWBStatuses());
            //// TODO: 2017/1/18 add retry
//                        .retry(8, RxUtil.retryIfWBServicePauseException());
        }
    }

    public Map<String, String> getWBAllStatusParameter(String id, final boolean isRefresh) {
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
        if (mStatusGroup.groupList != null) {
            ub.addParameter("list_id", mStatusGroup.groupList.getGid());
        }

        ub.addParameter("count", WBUtil.getWBStatusRequestCount());
        return ub.getParameters();
    }

    private SingleObserver <List<Status>> getStatusesSubscriber(final boolean isRefresh) {
        return new ListDataSingleObserver(isRefresh);
    }

//    Function<WBStatuses, SingleSource<ArrayList<Status>>> mFlatWBStatuses;
//
//    private Function<WBStatuses, SingleSource<ArrayList<Status>>> flatWBStatuses() {
//        if (mFlatWBStatuses == null) {
//            mFlatWBStatuses = new Function<WBStatuses, SingleSource<ArrayList<Status>>>() {
//                @Override
//                public SingleSource<ArrayList<Status>> apply(WBStatuses wbStatuses) {
//                    return SingleHelper.justArrayList(Status.getStatuses(wbStatuses));
////                    return ObservableHelper.just(Status.getStatuses(wbStatuses));
//                }
//            };
//        }
//        return mFlatWBStatuses;
//    }

//    Function<WBStatusComments, SingleSource<ArrayList<Status>>> mFlatWBStatusComments;
//
//    private Function<WBStatusComments, SingleSource<ArrayList<Status>>> flatWBStatusComments() {
//        if (mFlatWBStatusComments == null) {
//            mFlatWBStatusComments = new Function<WBStatusComments, SingleSource<ArrayList<Status>>>() {
//                @Override
//                public SingleSource<ArrayList<Status>> apply(WBStatusComments wbComments) {
//                    return SingleHelper.justArrayList(Status.getStatuses(wbComments));
//                }
//            };
//        }
//        return mFlatWBStatusComments;
//    }

    private Function<WBStatusIds, SingleSource<WBStatuses>> flatWBStatusId(final String since_id) {
        return new Function<WBStatusIds, SingleSource<WBStatuses>>() {
            @Override
            public SingleSource<WBStatuses> apply(WBStatusIds wbStatusIds) {

                SingleSource<WBStatuses> observable;
                if (wbStatusIds == null || CommonUtil.isEmpty(wbStatusIds.getStatuses())) {
                    //没有新的微博
                    L.debug("no topic update");
                    observable = Single.just(new WBStatuses());
                } else {
                    if (wbStatusIds.getStatuses().size() >= WBUtil.getWBStatusRequestCount()) {
                        //还有更新的微博，重新请求刷新
//                                RequestManager.addToRequestQueue(getWBStatusRequest(token, 0 + "", true), getRequestTag());
                        L.debug("exist newer topic, request loadStatus again");
                        observable = getWBStatuses("0", true);
                    } else {
                        //新的微博条数没有超过请求条数，显示更新多少条微博，根据请求的since_id获取微博
                        L.debug("new topic is less than MAX_COUNT_REQUEST, request loadStatus by since_id");
                        observable = getWBStatuses(since_id, true);
                    }
                }

                return observable;
            }
        };
    }

    /**
     * 先检测有没有缓存，没有再请求服务器
     */
    public void loadWBStatus(final String firstPage) {
        getMvpView().onTaskStart();
        SingleHelper.justArrayList(findData())
                .flatMap(new Function<ArrayList<Status>, SingleSource<ArrayList<Status>>>() {
                    @Override
                    public SingleSource<ArrayList<Status>> apply(ArrayList<Status> topics) {
                        if (CommonUtil.isEmpty(topics)) {
                            return getStatuses(firstPage, true, false);
                        } else {
                            return Single
                                    .just(topics)
                                    .delay(500, TimeUnit.MILLISECONDS);
                        }
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribeWith(getStatusesSubscriber(true));
    }

    public void deleteStatus(final Status topic){
        RetrofitManager
                .getWBService()
                .destroyStatus(UserUtil.getToken(), topic.getId())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<WBStatus>() {
                    @Override
                    public void onSuccess(StatusContract.View view, WBStatus wbStatus) {
                        view.deleteStatusResult(TaskState.STATE_SUCCESS, topic);
                    }

                    @Override
                    public void onError(StatusContract.View view, Throwable e) {
                        view.deleteStatusResult(TaskState.getFailState(e), null);
                    }
                });
    }

    public ArrayList<Status> findData() {
        if(isNeedCache()) {
            return ShareJson.findData(getModelName(), new TypeToken<ArrayList<Status>>() {
            }.getType());
        }
        return null;
    }

    public void saveData(List<Status> data) {
        if (isNeedCache()) {
            ShareJson.saveListData(getModelName(), data);
        }
    }

    public void clearCache(){
        ShareJson.saveListData(getModelName(), null);
    }

    public boolean isNeedCache() {
        return mStatusGroup.statusType != StatusType.HOMEPAGE || UserUtil.isCurrentUser(uid);
    }

    private String mModuleName;
    private String mModuleUid;

    public String getModelName() {
        if (mModuleName == null || mModuleUid == null || !mModuleUid.equals(UserUtil.getUid())) {
            mModuleUid = UserUtil.getUid();
            mModuleName = Status.class.getSimpleName()
                    + mModuleUid
                    + "/"
                    + uid
                    + "/"
                    + name
                    + "/"
                    + mStatusGroup;
        }
        return mModuleName;
    }

    public enum StatusType {
        ALL, BILATERAL, COMMENT_AT_ME, TOPIC_AT_ME, COMMENT_TO_ME, COMMENT_BY_ME, HOMEPAGE, GROUP_LIST, NONE
    }

    public static class StatusGroup implements Serializable {

        private static final long serialVersionUID = -5394322343417513819L;

        private StatusType statusType;

        public StatusGroup(StatusType statusType) {
            this.statusType = statusType;
        }

        public StatusGroup(StatusType statusType, GroupList groupList) {
            this.statusType = statusType;
            this.groupList = groupList;
        }

        private GroupList groupList;

        public GroupList getGroupList() {
            return groupList;
        }

        public void setGroupList(GroupList groupList) {
            this.groupList = groupList;
        }

        public StatusType getStatusType() {
            return statusType;
        }

        public void setStatusType(StatusType statusType) {
            this.statusType = statusType;
        }

        /**
         * @return 如果是首页的列表内容, 则返回true;
         */
        public boolean isReadingType() {
            return statusType == StatusType.ALL || statusType == StatusType.BILATERAL || statusType == StatusType.GROUP_LIST;
        }

        @Override
        public String toString() {
            if (groupList == null) {
                return statusType.toString();
            }

            return statusType
                    + "/"
                    + groupList.getGid();
        }

        public String getName() {
            return getName(this, ResUtil.getResources());
        }

        public static List<StatusGroup> getAllStatusGroups() {
            ArrayList<StatusGroup> statusGroupGroups = new ArrayList<>();
            statusGroupGroups.add(new StatusGroup(StatusType.ALL));
            statusGroupGroups.add(new StatusGroup(StatusType.BILATERAL));
            List<StatusGroup> temp = StatusGroup.getStatusGroups();
            if (!CommonUtil.isEmpty(temp)) {
                statusGroupGroups.addAll(temp);
            }

            return statusGroupGroups;
        }

        public static List<StatusGroup> getStatusGroups() {
            return getStatusGroups(UserUtil.queryGroupList());
        }

        public static List<StatusGroup> getStatusGroups(List<GroupList> groupLists) {
            if (CommonUtil.isEmpty(groupLists)) {
                return null;
            }
            List<StatusGroup> result = new ArrayList<>();
            for (GroupList gl : groupLists) {
                result.add(new StatusGroup(StatusType.GROUP_LIST, gl));
            }
            return result;
        }

        /**
         * @param groupIdStr
         * @return 返回与@param groupIdStr 对应的分组名字
         */
        public static String getStatusGroupName(String groupIdStr) {
            List<StatusGroup> statusGroups = StatusGroup.getStatusGroups();
            if (!CommonUtil.isEmpty(groupIdStr) && !CommonUtil.isEmpty(statusGroups)) {
                for (StatusGroup sg : statusGroups) {
                    if (groupIdStr.equals(sg.getGroupList().getGid())) {
                        return sg.getName();
                    }
                }
            }
            return null;
        }

        private static String getName(StatusGroup statusGroup, Resources resources) {
            switch (statusGroup.statusType) {
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
                    return resources.getString(R.string.title_page_status);
                case GROUP_LIST:
                    if (statusGroup.groupList == null) {
                        return "未知分组";
                    } else {
                        return statusGroup.groupList.getName();
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
