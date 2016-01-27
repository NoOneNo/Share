package com.hengye.share.ui.mvpview;

import com.hengye.share.model.greenrobot.GroupList;

import java.util.List;

public interface GroupManageMvpView extends MvpView {

    void loadSuccess();

    void loadFail();

    void handleGroupList(List<GroupList> groupLists);

    void updateGroupOrderCallBack(boolean isSuccess);

}
