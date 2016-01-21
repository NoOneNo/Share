package com.hengye.share.ui.mvpview;

import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;

import java.util.List;

public interface SearchMvpView extends MvpView {

    void loadSuccess();

    void loadFail();

    void handleSearchUserData(List<UserInfo> userInfos);

    void handleSearchPublicData(List<Topic> topics);
}
