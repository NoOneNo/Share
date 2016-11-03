package com.hengye.share.module.search;

import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.mvp.MvpView;

import java.util.List;

public interface SearchMvpView extends MvpView {

    void loadSuccess();

    void loadFail();

    void handleSearchUserData(List<UserInfo> userInfos);

    void handleSearchPublicData(List<Topic> topics);
}
