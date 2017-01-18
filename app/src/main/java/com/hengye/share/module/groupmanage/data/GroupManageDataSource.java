package com.hengye.share.module.groupmanage.data;

import com.hengye.share.model.greenrobot.GroupList;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by yuhy on 2016/10/3.
 */

public interface GroupManageDataSource {

    Single<ArrayList<GroupList>> getGroupList();

    Single<ArrayList<GroupList>> refreshGroupList();

    Single<Boolean> updateGroupList(List<GroupList> groupLists);

}
