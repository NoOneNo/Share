package com.hengye.share.module.groupmanage;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.module.mvp.BasePresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class GroupManagePresenter extends BasePresenter<GroupManageMvpView> {

    public GroupManagePresenter(GroupManageMvpView mvpView) {
        super(mvpView);
    }

    public void loadGroupList() {
        loadGroupList(true);
    }

    public void loadGroupList(boolean isReadCache) {

        if (isReadCache) {
            List<GroupList> list = UserUtil.queryGroupList();
            if (!CommonUtil.isEmpty(list)) {
                getMvpView().handleGroupList(true, list);
                return;
            }
        }

        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getPriorToken());
        addSubscription(
                RetrofitManager
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
                        })
                        .subscribeOn(SchedulerProvider.io())
                        .observeOn(SchedulerProvider.ui())
                        .subscribe(new BaseSubscriber<List<GroupList>>() {
                            @Override
                            public void onNext(GroupManageMvpView v, List<GroupList> groupLists) {
                                v.loadSuccess();
                                setIsGroupUpdate(true);
                                v.handleGroupList(false, groupLists);
                            }

                            @Override
                            public void onError(GroupManageMvpView v, Throwable e) {
                                super.onError(v, e);
                                v.loadFail();
                            }
                        }));


    }

    public void updateGroupOrder(final List<GroupList> data) {
        addSubscription(
                RetrofitManager
                        .getWBService()
                        .updateGroupOrder(UserUtil.getPriorToken(), data.size() + "", GroupList.getGroupIds(data))
                        .flatMap(new Func1<WBGroups.WBGroupUpdateOrder, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(WBGroups.WBGroupUpdateOrder result) {
//                        {"result":true}
                                boolean isSuccess = false;
                                if (result != null && "true".equals(result.getResult())) {
                                    isSuccess = true;
                                }
                                if (isSuccess) {
                                    setIsGroupUpdate(true);
                                    UserUtil.updateGroupList(data, false);
                                }
                                return Observable.just(isSuccess);
                            }
                        })
                        .subscribeOn(SchedulerProvider.io())
                        .observeOn(SchedulerProvider.ui())
                        .subscribe(new BaseSubscriber<Boolean>() {
                            @Override
                            public void onNext(GroupManageMvpView v, Boolean isSuccess) {
                                getMvpView().updateGroupOrderCallBack(isSuccess);
                            }

                            @Override
                            public void onError(GroupManageMvpView v, Throwable e) {
                                super.onError(v, e);
                                getMvpView().updateGroupOrderCallBack(false);
                            }
                        }));

    }

    public void checkGroupOrderIsChange(List<GroupList> groupLists) {
        addSubscription(
                Observable
                        .just(groupLists)
                        .flatMap(new Func1<List<GroupList>, Observable<Boolean>>() {
                            @Override
                            public Observable<Boolean> call(List<GroupList> groupLists) {
                                return Observable.just(!groupLists.equals(UserUtil.queryGroupList(false)));
                            }
                        })
                        .subscribeOn(SchedulerProvider.io())
                        .observeOn(SchedulerProvider.ui())
                        .subscribe(new BaseSubscriber<Boolean>() {
                            @Override
                            public void onNext(GroupManageMvpView v, Boolean isChange) {
                                setIsGroupUpdate(isChange);
                                getMvpView().checkGroupOrder(isChange);
                            }

                            @Override
                            public void onError(GroupManageMvpView v, Throwable e) {
                                super.onError(v, e);
                                getMvpView().checkGroupOrder(false);
                            }
                        }));
    }

    private boolean mIsGroupUpdate = false;

    public void setIsGroupUpdate(boolean isGroupUpdate) {
        this.mIsGroupUpdate = isGroupUpdate;
    }

    public boolean isGroupUpdate() {
        return mIsGroupUpdate;
    }
}
