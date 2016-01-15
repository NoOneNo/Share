package com.hengye.share.ui.mvpview;

import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.ui.base.MvpView;

import java.util.List;

public interface SearchMvpView extends MvpView {

    void handleSearchUserData(List<UserInfo> userInfos);

    void handleSearchPublicData(List<Topic> topics);
}
