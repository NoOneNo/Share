package com.hengye.share.module.groupmanage;

import android.support.annotation.NonNull;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.module.groupmanage.data.GroupManageDataSource;
import com.hengye.share.module.mvp.BasePresenter;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

public class GroupManagePresenter extends BasePresenter<GroupManageMvpView> {

    @NonNull
    private final GroupManageDataSource mGroupManageRepository;

    @NonNull
    private final SchedulerProvider mSchedulerProvider;

    public GroupManagePresenter(
            @NonNull GroupManageMvpView mvpView,
            @NonNull GroupManageDataSource groupManageRepository,
            @NonNull SchedulerProvider schedulerProvider) {
        super(mvpView);

        mGroupManageRepository = groupManageRepository;
        mSchedulerProvider = schedulerProvider;
    }

    public void loadGroupList() {
        addSubscription(
        mGroupManageRepository
                .getGroupList()
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSubscriber<List<GroupList>>() {
                    @Override
                    public void onNext(GroupManageMvpView v, List<GroupList> groupLists) {
                        v.loadSuccess();
                        v.handleGroupList(true, groupLists);
                    }

                    @Override
                    public void onError(GroupManageMvpView v, Throwable e) {
                        v.loadFail();
                    }
                }));
    }

    public void refreshGroupList() {
        addSubscription(
                mGroupManageRepository
                        .refreshGroupList()
                        .subscribeOn(SchedulerProvider.io())
                        .observeOn(SchedulerProvider.ui())
                        .subscribe(new BaseSubscriber<List<GroupList>>() {
                            @Override
                            public void onNext(GroupManageMvpView v, List<GroupList> groupLists) {
                                setIsGroupUpdate(true);
                                v.loadSuccess();
                                v.handleGroupList(true, groupLists);
                            }

                            @Override
                            public void onError(GroupManageMvpView v, Throwable e) {
                                v.loadFail();
                            }
                        }));
    }

    public void updateGroupOrder(final List<GroupList> data) {
        addSubscription(
                mGroupManageRepository
                        .updateGroupList(data)
                        .subscribeOn(SchedulerProvider.io())
                        .observeOn(SchedulerProvider.ui())
                        .subscribe(new BaseSubscriber<Boolean>() {
                            @Override
                            public void onNext(GroupManageMvpView v, Boolean isSuccess) {
                                v.updateGroupOrderCallBack(isSuccess);
                            }

                            @Override
                            public void onError(GroupManageMvpView v, Throwable e) {
                                v.updateGroupOrderCallBack(false);
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
                                v.checkGroupOrder(isChange);
                            }

                            @Override
                            public void onError(GroupManageMvpView v, Throwable e) {
                                v.checkGroupOrder(false);
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
