package com.hengye.share.module.groupmanage;

import android.support.annotation.NonNull;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.module.groupmanage.data.GroupManageDataSource;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GroupManagePresenter extends RxPresenter<GroupManageMvpView> {

    @NonNull
    private final GroupManageDataSource mGroupManageRepository;

    public GroupManagePresenter(
            @NonNull GroupManageMvpView mvpView,
            @NonNull GroupManageDataSource groupManageRepository) {
        super(mvpView);

        mGroupManageRepository = groupManageRepository;
    }

    public void loadGroupList() {
        mGroupManageRepository
                .getGroupList()
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new Consumer<List<GroupList>>() {
                    @Override
                    public void accept(List<GroupList> groupLists) throws Exception {
                        if (getMvpView() != null) {
                            getMvpView().loadSuccess();
                            getMvpView().handleGroupList(true, groupLists);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (getMvpView() != null) {
                            getMvpView().loadFail();
                        }
                    }
                });
    }

    public void refreshGroupList() {
        mGroupManageRepository
                .refreshGroupList()
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new Consumer<List<GroupList>>() {
                    @Override
                    public void accept(List<GroupList> groupLists) throws Exception {
                        if (getMvpView() != null) {
                            setIsGroupUpdate(true);
                            getMvpView().loadSuccess();
                            getMvpView().handleGroupList(true, groupLists);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (getMvpView() != null) {
                            getMvpView().loadFail();
                        }
                    }
                });
    }

    public void updateGroupOrder(final ArrayList<GroupList> data) {
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
                });
    }

    public void checkGroupOrderIsChange(@NonNull List<GroupList> groupLists) {
        ObservableHelper
                .justArrayList(groupLists)
                .flatMap(new Function<List<GroupList>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> apply(List<GroupList> groupLists) {
                        return ObservableHelper.just(!groupLists.equals(UserUtil.queryGroupList(false)));
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
                });
    }

    private boolean mIsGroupUpdate = false;

    public void setIsGroupUpdate(boolean isGroupUpdate) {
        this.mIsGroupUpdate = isGroupUpdate;
    }

    public boolean isGroupUpdate() {
        return mIsGroupUpdate;
    }
}
