package com.hengye.share.ui.presenter;

import android.text.TextUtils;

import com.hengye.share.model.Topic;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.ui.mvpview.TopicAlbumMvpView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TopicAlbumPresenter extends BasePresenter<TopicAlbumMvpView> {

    private String uid, name;
    private String currentId = "0";

    public TopicAlbumPresenter(TopicAlbumMvpView mvpView) {
        super(mvpView);
    }

    public void loadTopicAlbum(final boolean isRefresh) {
        RetrofitManager
                .getWBService()
                .listUserTopic(getParameter(isRefresh))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<WBTopics>() {
                    @Override
                    public void handleViewOnFail(TopicAlbumMvpView v, Throwable e) {
                        v.stopLoading(isRefresh);
                    }

                    @Override
                    public void handleViewOnSuccess(TopicAlbumMvpView v, WBTopics wbTopics) {

                        List<Topic> topics = Topic.getTopics(wbTopics);

                        if (isRefresh) {
                            currentId = "0";
                        } else {
                            if (!CommonUtil.isEmpty(topics)) {
                                currentId = topics.get(topics.size() - 1).getId();
                            }
                        }
                        v.stopLoading(isRefresh);
                        v.handleAlbumData(getImageUrls(topics), isRefresh);
                    }
                });
    }

    public List<String> getImageUrls(List<Topic> topics) {
        if (CommonUtil.isEmpty(topics)) {
            return null;
        }
        List<String> urls = new ArrayList<>();
        for (Topic topic : topics) {
            if (!CommonUtil.isEmpty(topic.getImageUrls())) {
                urls.addAll(topic.getImageUrls());
            }
//            if (topic.getRetweetedTopic() != null) {
//                if (!CommonUtil.isEmpty(topic.getRetweetedTopic().getImageUrls())) {
//                    urls.addAll(topic.getRetweetedTopic().getImageUrls());
//                }
//            }
        }
        return urls;
    }


    public Map<String, String> getParameter(final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder();
        String token = UserUtil.getAdToken();
        if (token == null) {
            token = UserUtil.getToken();
        }
        ub.addParameter("access_token", token);
        if (isRefresh) {
            ub.addParameter("since_id", currentId);
        } else {
            ub.addParameter("max_id", currentId);
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
