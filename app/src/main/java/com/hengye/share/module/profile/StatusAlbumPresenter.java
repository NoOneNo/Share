package com.hengye.share.module.profile;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.model.KeyValue;
import com.hengye.share.model.Status;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBStatuses;
import com.hengye.share.module.util.encapsulation.mvp.ListTaskPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class StatusAlbumPresenter extends ListTaskPresenter<StatusAlbumContract.View> {

    private String uid, name;

    public StatusAlbumPresenter(StatusAlbumContract.View mvpView) {
        super(mvpView);
    }

    public void loadStatusAlbum(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listUserStatus(getParameter(id, isRefresh))
                .flatMap(flatWBStatuses())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getStatusSubscriber(isRefresh));
    }

    public static KeyValue<ArrayList<Status>, ArrayList<String>> getImageUrls(List<Status> statuses) {
        if (CommonUtil.isEmpty(statuses)) {
            return new KeyValue<>(null, null);
        }
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<Status> imageStatuses = new ArrayList<>();
        for (Status topic : statuses) {
            if (!CommonUtil.isEmpty(topic.getImageUrls())) {
                for (String url : topic.getImageUrls()) {
                    urls.add(url);
                    imageStatuses.add(topic);
                }
            }
        }
        return new KeyValue<>(imageStatuses, urls);
    }


    public Map<String, String> getParameter(String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder();
        String token;
        if (uid != null && uid.equals(UserUtil.getUid())) {
            token = UserUtil.getToken();
        } else {
            token = UserUtil.getPriorToken();
        }

        ub.addParameter("access_token", token);
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

        // 原创相册
        ub.addParameter("feature", "12");

        ub.addParameter("count", WBUtil.getWBStatusRequestCount());
        return ub.getParameters();
    }

    public void loadCacheData() {

        SingleHelper
                .justArrayList(findData())
                .flatMap(new Function<ArrayList<Status>, SingleSource<ArrayList<Status>>>() {
                    @Override
                    public SingleSource<ArrayList<Status>> apply(ArrayList<Status> topics) throws Exception {
                        if (topics == null || topics.isEmpty()) {
                            return RetrofitManager
                                    .getWBService()
                                    .listUserStatus(getParameter("0", true))
                                    .flatMap(flatWBStatuses());
                        } else {
                            return SingleHelper.justArrayList(topics);
                        }
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getStatusSubscriber(true));
    }

    private SingleObserver<ArrayList<Status>> getStatusSubscriber(boolean isRefresh) {
        return new ListTaskSingleObserver<ArrayList<Status>>(isRefresh) {
            @Override
            public void onSuccess(StatusAlbumContract.View v, ArrayList<Status> topics) {
                super.onSuccess(v, topics);
                if (isRefresh) {
                    saveData(topics);
                }
                KeyValue<ArrayList<Status>, ArrayList<String>> kv = getImageUrls(topics);
                v.handleAlbumData(kv.getKey(), kv.getValue(), isRefresh);
            }
        };
    }

    private Function<WBStatuses, SingleSource<ArrayList<Status>>> mFlatWBStatuses;

    private Function<WBStatuses, SingleSource<ArrayList<Status>>> flatWBStatuses() {
        if (mFlatWBStatuses == null) {
            mFlatWBStatuses = new Function<WBStatuses, SingleSource<ArrayList<Status>>>() {
                @Override
                public SingleSource<ArrayList<Status>> apply(WBStatuses wbStatuses) throws Exception {
                    return SingleHelper.justArrayList(Status.getStatuses(wbStatuses));
                }
            };
        }
        return mFlatWBStatuses;
    }

    private ArrayList<Status> findData() {
        if(isNeedCache()) {
            return ShareJson.findData(getModelName(), new TypeToken<ArrayList<Status>>() {
            }.getType());
        }
        return null;
    }

    private void saveData(List<Status> data) {
        if (isNeedCache()) {
            ShareJson.saveListData(getModelName(), data);
        }
    }

    private boolean isNeedCache() {
        return UserUtil.isCurrentUser(uid);
    }


    private String mModuleName;

    private String getModelName() {
        if (mModuleName == null) {
            mModuleName = StatusAlbumPresenter.class.getSimpleName()
                    + uid
                    + "/"
                    + name;
        }
        return mModuleName;
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
