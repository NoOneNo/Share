package com.hengye.share.module.profile;

import com.hengye.share.model.Status;
import com.hengye.share.module.util.encapsulation.mvp.ListTaskMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;

import java.util.ArrayList;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface StatusAlbumContract {

    interface View extends ListTaskMvpView {
        void handleAlbumData(ArrayList<Status> topics, ArrayList<String> urls, boolean isRefresh);
    }

    interface Presenter extends MvpPresenter<View> {
    }
}
