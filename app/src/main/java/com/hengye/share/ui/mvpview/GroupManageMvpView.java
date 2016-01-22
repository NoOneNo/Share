package com.hengye.share.ui.mvpview;

import com.hengye.share.model.sina.WBGroups;

public interface GroupManageMvpView extends MvpView {

    void loadSuccess();

    void loadFail();

    void handleGroupList(WBGroups wbGroups);

}
