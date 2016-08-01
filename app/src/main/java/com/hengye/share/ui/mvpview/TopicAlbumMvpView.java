package com.hengye.share.ui.mvpview;

import com.hengye.share.model.Topic;

import java.util.ArrayList;

public interface TopicAlbumMvpView extends MvpView {

    void stopLoading(boolean isRefresh);

    void handleAlbumData(ArrayList<Topic> topics, ArrayList<String> urls, boolean isRefresh);

    void handleCache(ArrayList<Topic> topics, ArrayList<String> urls);
}
