package com.hengye.share.module.groupmanage.data;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.util.rxjava.RxUtil;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by yuhy on 2016/10/3.
 */

public class GroupManageRepository implements GroupManageDataSource {

    GroupManageLocalDataSource mGroupManageLocalDataSource;

    GroupManageRemoteDataSource mGroupManageRemoteDataSource;

    public GroupManageRepository() {
        this(new GroupManageLocalDataSource(), new GroupManageRemoteDataSource());
    }

    public GroupManageRepository(GroupManageLocalDataSource groupManageLocalDataSource, GroupManageRemoteDataSource groupManageRemoteDataSource) {
        mGroupManageLocalDataSource = groupManageLocalDataSource;
        mGroupManageRemoteDataSource = groupManageRemoteDataSource;
    }

    @Override
    public Single<ArrayList<GroupList>> getGroupList() {

        Single<ArrayList<GroupList>> localGroups = mGroupManageLocalDataSource.getGroupList();
        Single<ArrayList<GroupList>> remoteGroups = mGroupManageRemoteDataSource.getGroupList();

        return RxUtil.filterEmpty(localGroups, remoteGroups);
    }

    @Override
    public Single<ArrayList<GroupList>> refreshGroupList() {
        return mGroupManageRemoteDataSource
                .getGroupList()
                .doOnSuccess(new Consumer<ArrayList<GroupList>>() {
                    @Override
                    public void accept(ArrayList<GroupList> groupLists) {
                        RxUtil.subscribeIgnoreAll(mGroupManageLocalDataSource.updateGroupList(groupLists));
                    }
                });
    }

    @Override
    public Observable<Boolean> updateGroupList(List<GroupList> groupLists) {
        final Observable<Boolean> localResult = mGroupManageLocalDataSource.updateGroupList(groupLists);
        Observable<Boolean> remoteResult = mGroupManageRemoteDataSource.updateGroupList(groupLists);

        return remoteResult.flatMap(new Function<Boolean, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> apply(Boolean isSuccess) {
                if (isSuccess) {
                    return localResult;
                }
                return ObservableHelper.just(false);
            }
        });
    }
}











