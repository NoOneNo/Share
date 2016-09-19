package com.hengye.share.module.profile;

import com.hengye.share.model.Topic;
import com.hengye.share.module.mvp.MvpView;

import java.util.ArrayList;

public interface TopicAlbumMvpView extends MvpView {

    void stopLoading(boolean isRefresh);

    void handleAlbumData(ArrayList<Topic> topics, ArrayList<String> urls, boolean isRefresh);

    void handleCache(ArrayList<Topic> topics, ArrayList<String> urls);
}
