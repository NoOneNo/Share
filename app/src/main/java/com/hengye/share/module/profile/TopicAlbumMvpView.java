package com.hengye.share.module.profile;

import com.hengye.share.model.Topic;
import com.hengye.share.module.util.encapsulation.mvp.ListTaskMvpView;

import java.util.ArrayList;

public interface TopicAlbumMvpView extends ListTaskMvpView {

    void handleAlbumData(ArrayList<Topic> topics, ArrayList<String> urls, boolean isRefresh);
}
