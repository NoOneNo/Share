package com.hengye.share.module.search;

import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

import java.util.List;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface SearchContract {

    interface View extends MvpView {
        void loadSuccess();

        void loadFail();

        void handleSearchUserData(List<UserInfo> userInfos);

        void handleSearchPublicData(List<Topic> topics);
    }

    interface Presenter extends MvpPresenter<View> {

        void loadWBSearchContent(String content);
    }
}
