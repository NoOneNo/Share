package com.hengye.share.module.groupmanage;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.module.mvp.BasePresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;

import java.util.List;

import rx.Observable;
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
            if(!CommonUtil.isEmpty(list)){
                getMvpView().handleGroupList(true, list);
                return;
            }
        }

        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getPriorToken());
        RetrofitManager
                .getWBService()
                .listGroups(ub.getParameters())
                .flatMap(new Func1<WBGroups, Observable<List<GroupList>>>() {
                    @Override
                    public Observable<List<GroupList>> call(WBGroups wbGroups) {

                        if(wbGroups == null || CommonUtil.isEmpty(wbGroups.getLists())){
                            return null;
                        }

                        List<GroupList> list = GroupList.getGroupLists(wbGroups, UserUtil.getUid());
                        UserUtil.updateGroupList(list);
                        return Observable.just(list);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<GroupList>>() {
                    @Override
                    public void handleViewOnSuccess(GroupManageMvpView v, List<GroupList> groupLists) {
                        getMvpView().loadSuccess();
                        setIsGroupUpdate(true);
                        getMvpView().handleGroupList(false, groupLists);
                    }

                    @Override
                    public void handleViewOnFail(GroupManageMvpView v, Throwable e) {
                        super.handleViewOnFail(v, e);
                        getMvpView().loadFail();
                    }
                });


    }

    public void updateGroupOrder(final List<GroupList> data){
        RetrofitManager
                .getWBService()
                .updateGroupOrder(UserUtil.getPriorToken(), data.size() + "", GroupList.getGroupIds(data))
                .flatMap(new Func1<WBGroups.WBGroupUpdateOrder, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(WBGroups.WBGroupUpdateOrder result) {
//                        {"result":true}
                        boolean isSuccess = false;
                        if(result != null && "true".equals(result.getResult())){
                            isSuccess = true;
                        }
                        if(isSuccess){
                            setIsGroupUpdate(true);
                            UserUtil.updateGroupList(data, false);
                        }
                        return Observable.just(isSuccess);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Boolean>() {
                    @Override
                    public void handleViewOnSuccess(GroupManageMvpView v, Boolean isSuccess) {
                        getMvpView().updateGroupOrderCallBack(isSuccess);
                    }

                    @Override
                    public void handleViewOnFail(GroupManageMvpView v, Throwable e) {
                        super.handleViewOnFail(v, e);
                        getMvpView().updateGroupOrderCallBack(false);
                    }
                });

    }

    public void checkGroupOrderIsChange(List<GroupList> groupLists){
        Observable
                .just(groupLists)
                .flatMap(new Func1<List<GroupList>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(List<GroupList> groupLists) {
                        return Observable.just(!groupLists.equals(UserUtil.queryGroupList(false)));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Boolean>() {
                    @Override
                    public void handleViewOnSuccess(GroupManageMvpView v, Boolean isChange) {
                        setIsGroupUpdate(isChange);
                        getMvpView().checkGroupOrder(isChange);
                    }

                    @Override
                    public void handleViewOnFail(GroupManageMvpView v, Throwable e) {
                        super.handleViewOnFail(v, e);
                        getMvpView().checkGroupOrder(false);
                    }
                });
    }

    private boolean mIsGroupUpdate = false;

    public void setIsGroupUpdate(boolean isGroupUpdate) {
        this.mIsGroupUpdate = isGroupUpdate;
    }

    public boolean isGroupUpdate(){
        return mIsGroupUpdate;
    }
}
