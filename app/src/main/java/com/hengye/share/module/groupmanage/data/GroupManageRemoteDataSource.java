package com.hengye.share.module.groupmanage.data;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.SingleHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
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
                .flatMap(new Function<WBGroups, SingleSource<ArrayList<GroupList>>>() {
                    @Override
                    public SingleSource<ArrayList<GroupList>> apply(WBGroups wbGroups) {

                        ArrayList<GroupList> list = GroupList.getGroupLists(wbGroups, UserUtil.getUid());
                        UserUtil.updateGroupList(list);
                        return SingleHelper.justArrayList(list);
                    }
                });
    }

    @Override
    public Single<Boolean> updateGroupList(final List<GroupList> groupLists) {
        return RetrofitManager
                .getWBService()
                .updateGroupOrder(UserUtil.getPriorToken(), groupLists.size() + "", GroupList.getGroupIds(groupLists))
                .flatMap(new Function<WBGroups.WBGroupUpdateOrder, SingleSource<? extends Boolean>>() {
                    @Override
                    public SingleSource<? extends Boolean> apply(WBGroups.WBGroupUpdateOrder result) throws Exception {
//                        {"result":true}
                        boolean isSuccess = false;
                        if (result != null && "true".equals(result.getResult())) {
                            isSuccess = true;
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
