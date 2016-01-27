package com.hengye.share.ui.presenter;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.ui.mvpview.GroupManageMvpView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GroupManagePresenter extends BasePresenter<GroupManageMvpView> {

    public GroupManagePresenter(GroupManageMvpView mvpView){
        super(mvpView);
    }

    public void loadGroupList(){
        loadGroupList(true);
    }

    public void loadGroupList(boolean isReadCache){

        if(isReadCache){
            List<GroupList> list = UserUtil.queryGroupList();
            if(!CommonUtil.isEmptyCollection(list)){
                getMvpView().handleGroupList(list);
                return;
            }
        }

        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getToken());
        RetrofitManager
                .getWBService()
                .listGroups(ub.getParameters())
                .flatMap(new Func1<WBGroups, Observable<List<GroupList>>>() {
                    @Override
                    public Observable<List<GroupList>> call(WBGroups wbGroups) {

                        if(wbGroups == null || CommonUtil.isEmptyCollection(wbGroups.getLists())){
                            return null;
                        }

                        List<GroupList> list = GroupList.getGroupLists(wbGroups, UserUtil.getUid());
                        UserUtil.updateGroupList(list);
                        return Observable.just(list);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GroupList>>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().loadSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().loadFail();
                    }

                    @Override
                    public void onNext(List<GroupList> groupLists) {

                        getMvpView().handleGroupList(groupLists);
//                        GroupList.getGroupLists(wbGroups, UserUtil.getUid());
//                        UserUtil.updateGroupList(wbGroups);
//                        getMvpView().handleGroupList(wbGroups);
                    }
                });
    }

    public void updateGroupOrder(final List<GroupList> data){
        RetrofitManager
                .getWBService()
                .updateGroupOrder(UserUtil.getToken(), data.size() + "", GroupList.getGroupIds(data))
                .flatMap(new Func1<WBGroups.WBGroupUpdateOrder, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(WBGroups.WBGroupUpdateOrder result) {
//                        {"result":true}
                        boolean isSuccess = false;
                        if(result != null && "true".equals(result.getResult())){
                            isSuccess = true;
                        }
                        if(isSuccess){
                            List<GroupList> temp = new ArrayList<>(data);
                            UserUtil.updateGroupList(temp);
                        }
                        return Observable.just(isSuccess);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().updateGroupOrderCallBack(false);
                    }

                    @Override
                    public void onNext(Boolean isSuccess) {
                        getMvpView().updateGroupOrderCallBack(isSuccess);
//                        GroupList.getGroupLists(wbGroups, UserUtil.getUid());
//                        UserUtil.updateGroupList(wbGroups);
//                        getMvpView().handleGroupList(wbGroups);
                    }
                });

    }

}
