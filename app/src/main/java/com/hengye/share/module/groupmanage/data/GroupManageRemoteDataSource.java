package com.hengye.share.module.groupmanage.data;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yuhy on 2016/10/3.
 */

public class GroupManageRemoteDataSource implements GroupManageDataSource {

    @Override
    public Observable<List<GroupList>> getGroupList() {

        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getPriorToken());

        return RetrofitManager
                .getWBService()
                .listGroups(ub.getParameters())
                .flatMap(new Func1<WBGroups, Observable<List<GroupList>>>() {
                    @Override
                    public Observable<List<GroupList>> call(WBGroups wbGroups) {

                        if (wbGroups == null || CommonUtil.isEmpty(wbGroups.getLists())) {
                            return null;
                        }

                        List<GroupList> list = GroupList.getGroupLists(wbGroups, UserUtil.getUid());
                        UserUtil.updateGroupList(list);
                        return Observable.just(list);
                    }
                });
    }

    @Override
    public Observable<Boolean> updateGroupList(final List<GroupList> groupLists) {
        return RetrofitManager
                .getWBService()
                .updateGroupOrder(UserUtil.getPriorToken(), groupLists.size() + "", GroupList.getGroupIds(groupLists))
                .flatMap(new Func1<WBGroups.WBGroupUpdateOrder, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(WBGroups.WBGroupUpdateOrder result) {
//                        {"result":true}
                        boolean isSuccess = false;
                        if (result != null && "true".equals(result.getResult())) {
                            isSuccess = true;
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
