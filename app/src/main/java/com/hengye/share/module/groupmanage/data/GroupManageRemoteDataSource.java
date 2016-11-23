package com.hengye.share.module.groupmanage.data;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/**
 * Created by yuhy on 2016/10/3.
 */

public class GroupManageRemoteDataSource implements GroupManageDataSource {

    @Override
    public Single<ArrayList<GroupList>> getGroupList() {

        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getPriorToken());

        return RetrofitManager
                .getWBService()
                .listGroups(ub.getParameters())
                .flatMap(new Function<WBGroups, Observable<ArrayList<GroupList>>>() {
                    @Override
                    public Observable<ArrayList<GroupList>> apply(WBGroups wbGroups) {

                        ArrayList<GroupList> list = GroupList.getGroupLists(wbGroups, UserUtil.getUid());

                        UserUtil.updateGroupList(list);
                        return ObservableHelper.justArrayList(list);
                    }
                })
                .firstOrError();
    }

    @Override
    public Observable<Boolean> updateGroupList(final List<GroupList> groupLists) {
        return RetrofitManager
                .getWBService()
                .updateGroupOrder(UserUtil.getPriorToken(), groupLists.size() + "", GroupList.getGroupIds(groupLists))
                .flatMap(new Function<WBGroups.WBGroupUpdateOrder, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> apply(WBGroups.WBGroupUpdateOrder result) {
//                        {"result":true}
                        boolean isSuccess = false;
                        if (result != null && "true".equals(result.getResult())) {
                            isSuccess = true;
                        }
                        return ObservableHelper.just(isSuccess);
                    }
                });
    }

    @Override
    public Single<ArrayList<GroupList>> refreshGroupList() {
        return null;
    }

}
