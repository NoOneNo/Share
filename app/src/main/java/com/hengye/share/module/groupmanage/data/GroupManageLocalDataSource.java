package com.hengye.share.module.groupmanage.data;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.util.UserUtil;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yuhy on 2016/10/3.
 */

public class GroupManageLocalDataSource implements GroupManageDataSource {

    @Override
    public Observable<List<GroupList>> getGroupList() {
        return Observable.just(UserUtil.queryGroupList());
    }

    @Override
    public Observable<Boolean> updateGroupList(List<GroupList> groupLists) {
        return Observable
                .just(groupLists)
                .flatMap(new Func1<List<GroupList>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<GroupList> groupLists) {
                        boolean isSuccess = true;
                        try {
                            UserUtil.updateGroupList(groupLists, false);
                        }catch (Exception e){
                            e.printStackTrace();
                            isSuccess = false;
                        }
                        return Observable.just(isSuccess);
                    }
                });
    }

    @Override
    public Observable<List<GroupList>> refreshGroupList() {
        return null;
    }
}
