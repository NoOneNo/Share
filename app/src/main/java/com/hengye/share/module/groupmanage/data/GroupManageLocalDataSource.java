package com.hengye.share.module.groupmanage.data;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;
import com.hengye.share.util.rxjava.datasource.SingleHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

/**
 * Created by yuhy on 2016/10/3.
 */

public class GroupManageLocalDataSource implements GroupManageDataSource {

    @Override
    public Single<ArrayList<GroupList>> getGroupList() {

        return SingleHelper.justArrayList(UserUtil.queryGroupList());
    }

    @Override
    public Single<Boolean> updateGroupList(List<GroupList> groupLists) {
        return SingleHelper
                .justArrayList(groupLists)
                .flatMap(new Function<ArrayList<GroupList>, SingleSource<Boolean>>() {
                    @Override
                    public SingleSource<Boolean> apply(ArrayList<GroupList> groupLists) throws Exception {
                        boolean isSuccess = true;
                        try {
                            UserUtil.updateGroupList(groupLists, false);
                        }catch (Exception e){
                            e.printStackTrace();
                            isSuccess = false;
                        }
                        return Single.just(isSuccess);
                    }
                });
    }

    @Override
    public Single<ArrayList<GroupList>> refreshGroupList() {
        return null;
    }
}
