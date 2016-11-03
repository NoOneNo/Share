package com.hengye.share.module.profile;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.model.KeyValue;
import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.ShareJson;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.module.util.encapsulation.mvp.TaskPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

public class TopicAlbumPresenter extends TaskPresenter<TopicAlbumMvpView> {

    private String uid, name;

    public TopicAlbumPresenter(TopicAlbumMvpView mvpView) {
        super(mvpView);
    }

    public void loadTopicAlbum(String id, final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listUserTopic(getParameter(id, isRefresh))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new TaskSubscriber<WBTopics>(isRefresh) {
                    @Override
                    public void onNext(TopicAlbumMvpView v, WBTopics wbTopics) {

                        ArrayList<Topic> topics = Topic.getTopics(wbTopics);

                        if(isRefresh){
                            saveData(topics);
                        }
                        KeyValue<ArrayList<Topic>, ArrayList<String>> kv = getImageUrls(topics);
                        v.handleAlbumData(kv.getKey(), kv.getValue(), isRefresh);
                    }
                });
    }

    public static KeyValue<ArrayList<Topic>, ArrayList<String>> getImageUrls(List<Topic> topics) {
        if (CommonUtil.isEmpty(topics)) {
            return new KeyValue<>(null, null);
        }
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<Topic> imageTopics = new ArrayList<>();
        for (Topic topic : topics) {
            if (!CommonUtil.isEmpty(topic.getImageUrls())) {
                for(String url : topic.getImageUrls()) {
                    urls.add(url);
                    imageTopics.add(topic);
                }
            }
        }
        return new KeyValue<>(imageTopics, urls);
    }


    public Map<String, String> getParameter(String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder();
        String token = UserUtil.getPriorToken();
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

    public void loadCacheData(){
        Observable
                .create(new Observable.OnSubscribe<ArrayList<Topic>>() {
                    @Override
                    public void call(Subscriber<? super ArrayList<Topic>> subscriber) {
                        subscriber.onNext(findData());
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSubscriber<ArrayList<Topic>>() {
                    @Override
                    public void onNext(TopicAlbumMvpView v, ArrayList<Topic> topics) {
                        KeyValue<ArrayList<Topic>, ArrayList<String>> kv = getImageUrls(topics);
                        v.handleCache(kv.getKey(), kv.getValue());
                    }

                    @Override
                    public void onError(TopicAlbumMvpView v, Throwable e) {

                    }
                });
    }

    public ArrayList<Topic> findData() {
        return ShareJson.findData(getModelName(), new TypeToken<ArrayList<Topic>>(){}.getType());
    }

    public void saveData(List<Topic> data) {
        ShareJson.saveListData(getModelName(), data);
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
