package com.hengye.share.module.groupmanage;

import android.support.annotation.NonNull;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.module.util.encapsulation.mvp.ListTaskMvpView;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 2017/1/16.
 */

public interface GroupManageContract {

    interface View extends ListTaskMvpView {

        void loadGroupList(boolean isCache, List<GroupList> groupLists);

        void updateGroupOrderCallBack(int taskState);

        void checkGroupOrder(boolean isChange);

        void createGroupResult(int taskState, GroupList groupList);

        void updateGroupResult(int taskState, GroupList groupList);

        void deleteGroupResult(int taskState, GroupList groupList);

    }

    interface Presenter extends MvpPresenter<View> {

        void loadGroupList();

        void refreshGroupList();

        void updateGroupOrder(ArrayList<GroupList> data);

        void checkGroupOrderIsChange(@NonNull List<GroupList> groupLists);

        void createGroup(String name, String description);

        void updateGroup(String gid, String name, String description);

        void deleteGroup(String gid);
    }
}
