package com.hengye.share.module.status;

import android.content.res.Resources;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.StatusFavorites;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBStatusFavorites;
import com.hengye.share.model.sina.WBStatuses;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.handler.StatusNumberPager;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class StatusPagePresenter extends ListDataPresenter<Status, StatusPageContract.View> {

    private StatusGroup mStatusGroup;
    private String mKeyword;
    private String uid, name;
    private StatusNumberPager mPager;

    public StatusPagePresenter(StatusPageContract.View mvpView, StatusGroup statusGroup, StatusNumberPager pager) {
        super(mvpView);
        mStatusGroup = statusGroup;
        mPager = pager;
    }

    public void loadRemoteWBStatus(boolean isRefresh) {
        getStatuses(isRefresh)
                .flatMap(StatusRxUtil.flatStatusShortUrl())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getStatusesSubscriber(isRefresh));
    }

    private Single<ArrayList<Status>> getStatuses(final boolean isRefresh) {
        switch (mStatusGroup.statusType) {
            case THEME:
                return RetrofitManager
                        .getWBService()
                        .searchStatus(getWBAllStatusParameter(mPager.getPage(isRefresh)))
                        .flatMap(flatWBStatuses());
            case FAVORITES:
            default:
                return RetrofitManager
                        .getWBService()
                        .listFavoritesStatus(getWBAllStatusParameter(mPager.getPage(isRefresh)))
                        .flatMap(flatWBStatusFavorites());
        }
    }

    private Map<String, String> getWBAllStatusParameter(int page) {
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

    private SingleObserver<List<Status>> getStatusesSubscriber(final boolean isRefresh) {
        return new ListDataSingleObserver(isRefresh);
    }

    Function<WBStatuses, SingleSource<ArrayList<Status>>> mFlatWBStatuses;

    private Function<WBStatuses, SingleSource<ArrayList<Status>>> flatWBStatuses() {
        if (mFlatWBStatuses == null) {
            mFlatWBStatuses = new Function<WBStatuses, SingleSource<ArrayList<Status>>>() {
                @Override
                public SingleSource<ArrayList<Status>> apply(WBStatuses wbStatuses) {
                    return SingleHelper.justArrayList(Status.getStatuses(wbStatuses));
                }
            };
        }
        return mFlatWBStatuses;
    }

    Function<WBStatusFavorites, SingleSource<ArrayList<Status>>> mFlatWBStatusFavorites;

    private Function<WBStatusFavorites, SingleSource<ArrayList<Status>>> flatWBStatusFavorites() {
        if (mFlatWBStatusFavorites == null) {
            mFlatWBStatusFavorites = new Function<WBStatusFavorites, SingleSource<ArrayList<Status>>>() {
                @Override
                public SingleSource<ArrayList<Status>> apply(WBStatusFavorites wbStatusFavorites) {
                    return SingleHelper.justArrayList(StatusFavorites.getStatues(wbStatusFavorites));
                }
            };
        }
        return mFlatWBStatusFavorites;
    }

    /**
     * 先检测有没有缓存，没有再请求服务器
     */
    public void loadWBStatus() {
        getMvpView().onTaskStart();


        SingleHelper
                .justArrayList(findData())
                .flatMap(new Function<ArrayList<Status>, SingleSource<ArrayList<Status>>>() {
                    @Override
                    public SingleSource<ArrayList<Status>> apply(ArrayList<Status> topics) {
                        if (CommonUtil.isEmpty(topics)) {
                            return getStatuses(true);
                        } else {
                            return Single
                                    .just(topics)
                                    .delay(400, TimeUnit.MILLISECONDS);
                        }
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribeWith(getStatusesSubscriber(true));
    }

    public ArrayList<Status> findData() {
        if (mStatusGroup.getStatusType() == StatusType.FAVORITES) {
            return ShareJson.findData(getModelName(), new TypeToken<ArrayList<Status>>() {
            }.getType());
        }else{
            return null;
        }
    }

    public void saveData(List<Status> data) {
        if(isNeedCache()) {
            ShareJson.saveListData(getModelName(), data);
        }
    }

    public boolean isNeedCache() {
        return mStatusGroup.statusType != StatusType.THEME;
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
        THEME, FAVORITES
    }

    public static class StatusGroup implements Serializable {

        private static final long serialVersionUID = -5809183018190271012L;

        private StatusType statusType;

        public StatusGroup(StatusType statusType) {
            this.statusType = statusType;
        }

        public StatusType getStatusType() {
            return statusType;
        }

        public void setStatusType(StatusType statusType) {
            this.statusType = statusType;
        }

        @Override
        public String toString() {
            return statusType.toString();
        }

        public static String getName(StatusGroup statusGroup, Resources resources) {
            switch (statusGroup.statusType) {
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
