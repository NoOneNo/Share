package com.hengye.share.module.groupmanage.data;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.rxjava.RxUtil;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by yuhy on 2016/10/3.
 */

public class GroupManageRepository implements GroupManageDataSource{

    GroupManageLocalDataSource mGroupManageLocalDataSource;

    GroupManageRemoteDataSource mGroupManageRemoteDataSource;

    public GroupManageRepository(){
        this(new GroupManageLocalDataSource(), new GroupManageRemoteDataSource());
    }

    public GroupManageRepository(GroupManageLocalDataSource groupManageLocalDataSource, GroupManageRemoteDataSource groupManageRemoteDataSource) {
        mGroupManageLocalDataSource = groupManageLocalDataSource;
        mGroupManageRemoteDataSource = groupManageRemoteDataSource;
    }

    @Override
    public Observable<List<GroupList>> getGroupList() {

        Observable<List<GroupList>> localGroups = mGroupManageLocalDataSource.getGroupList();
        Observable<List<GroupList>> remoteGroups = mGroupManageRemoteDataSource.getGroupList();

        return RxUtil.filterEmpty(localGroups, remoteGroups);
    }

    @Override
    public Observable<List<GroupList>> refreshGroupList() {
        return mGroupManageRemoteDataSource
                .getGroupList()
                .doOnNext(new Action1<List<GroupList>>() {
                    @Override
                    public void call(List<GroupList> groupLists) {
                        UserUtil.updateGroupList(groupLists);
                    }
                });
    }

    @Override
    public Observable<Boolean> updateGroupOrder(List<GroupList> groupLists) {
        final Observable<Boolean> localResult = mGroupManageLocalDataSource.updateGroupOrder(groupLists);
        Observable<Boolean> remoteResult = mGroupManageRemoteDataSource.updateGroupOrder(groupLists);

        return remoteResult.flatMap(new Func1<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Boolean isSuccess) {
                if(isSuccess){
                    return localResult;
                }
                return Observable.just(false);
            }
        });
    }
}











