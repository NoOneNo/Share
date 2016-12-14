package com.hengye.share.module.profile;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.model.KeyValue;
import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.module.util.encapsulation.mvp.ListTaskPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

public class TopicAlbumPresenter extends ListTaskPresenter<TopicAlbumMvpView> {

    private String uid, name;

    public TopicAlbumPresenter(TopicAlbumMvpView mvpView) {
        super(mvpView);
    }

    public void loadTopicAlbum(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listUserTopic(getParameter(id, isRefresh))
                .flatMap(flatWBTopics())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getTopicSubscriber(isRefresh));
    }

    public static KeyValue<ArrayList<Topic>, ArrayList<String>> getImageUrls(List<Topic> topics) {
        if (CommonUtil.isEmpty(topics)) {
            return new KeyValue<>(null, null);
        }
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<Topic> imageTopics = new ArrayList<>();
        for (Topic topic : topics) {
            if (!CommonUtil.isEmpty(topic.getImageUrls())) {
                for (String url : topic.getImageUrls()) {
                    urls.add(url);
                    imageTopics.add(topic);
                }
            }
        }
        return new KeyValue<>(imageTopics, urls);
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

        ub.addParameter("count", WBUtil.getWBTopicRequestCount());
        return ub.getParameters();
    }

    public void loadCacheData() {
//        Observable
//                .create(new ObservableOnSubscribe<ArrayList<Topic>>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<ArrayList<Topic>> subscriber) {
//                        ArrayList<Topic> topics = findData();
//                        if (topics == null) {
//                            topics = new ArrayList<>();
//                        }
//                        subscriber.onNext(topics);
//                    }
//                })

        ObservableHelper
                .justArrayList(findData())
                .flatMap(new Function<ArrayList<Topic>, ObservableSource<ArrayList<Topic>>>() {
                    @Override
                    public ObservableSource<ArrayList<Topic>> apply(ArrayList<Topic> topics) throws Exception {
                        if (topics == null || topics.isEmpty()) {
                            return RetrofitManager
                                    .getWBService()
                                    .listUserTopic(getParameter("0", true))
                                    .flatMap(flatWBTopics());
                        } else {
                            return ObservableHelper.justArrayList(topics);
                        }
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getTopicSubscriber(true));
    }

    private Observer<ArrayList<Topic>> getTopicSubscriber(boolean isRefresh) {
        return new ListTaskSubscriber<ArrayList<Topic>>(isRefresh) {
            @Override
            public void onNext(TopicAlbumMvpView v, ArrayList<Topic> topics) {

                if (isRefresh) {
                    saveData(topics);
                }
                KeyValue<ArrayList<Topic>, ArrayList<String>> kv = getImageUrls(topics);
                v.handleAlbumData(kv.getKey(), kv.getValue(), isRefresh);
            }
        };
    }

    private Function<WBTopics, Observable<ArrayList<Topic>>> mFlatWBTopics;

    private Function<WBTopics, Observable<ArrayList<Topic>>> flatWBTopics() {
        if (mFlatWBTopics == null) {
            mFlatWBTopics = new Function<WBTopics, Observable<ArrayList<Topic>>>() {
                @Override
                public Observable<ArrayList<Topic>> apply(WBTopics wbTopics) throws Exception {
                    return ObservableHelper.justArrayList(Topic.getTopics(wbTopics));
                }
            };
        }
        return mFlatWBTopics;
    }

    public ArrayList<Topic> findData() {
        if(isNeedCache()) {
            return ShareJson.findData(getModelName(), new TypeToken<ArrayList<Topic>>() {
            }.getType());
        }
        return null;
    }

    public void saveData(List<Topic> data) {
        if (isNeedCache()) {
            ShareJson.saveListData(getModelName(), data);
        }
    }

    public boolean isNeedCache() {
        return UserUtil.isCurrentUser(uid);
    }


    private String mModuleName;

    public String getModelName() {
        if (mModuleName == null) {
            mModuleName = TopicAlbumPresenter.class.getSimpleName()
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
