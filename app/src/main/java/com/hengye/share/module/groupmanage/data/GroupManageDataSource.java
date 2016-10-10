package com.hengye.share.module.groupmanage.data;

import com.hengye.share.model.greenrobot.GroupList;

import java.util.List;

import rx.Observable;

/**
 * Created by yuhy on 2016/10/3.
 */

public interface GroupManageDataSource {

    Observable<List<GroupList>> getGroupList();

    Observable<List<GroupList>> refreshGroupList();

    Observable<Boolean> updateGroupList(List<GroupList> groupLists);
//    List<GroupList>


}
