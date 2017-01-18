package com.hengye.share.module.profile;

import com.hengye.share.model.Topic;
import com.hengye.share.module.util.encapsulation.mvp.ListTaskMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface TopicAlbumContract {

    interface View extends ListTaskMvpView {
        void handleAlbumData(ArrayList<Topic> topics, ArrayList<String> urls, boolean isRefresh);
    }

    interface Presenter extends MvpPresenter<View> {
    }
}
