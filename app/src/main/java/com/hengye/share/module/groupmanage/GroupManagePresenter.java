package com.hengye.share.module.groupmanage;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.sina.WBGroup;
import com.hengye.share.module.groupmanage.data.GroupManageDataSource;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
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
                            getMvpView().handleGroupList(false, groupLists);
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

    public void createGroup(String name, String description){
        final UrlBuilder ub = new UrlBuilder();
        final String uid = UserUtil.getUid();
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("name", name);
        if(!TextUtils.isEmpty(description)){
            ub.addParameter("description", description);
        }
        RetrofitManager
                .getWBService()
                .createGroup(ub.getParameters())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSubscriber<WBGroup>() {
                    @Override
                    public void onNext(GroupManageMvpView v, WBGroup wbGroup) {
                        v.createGroupResult(TaskState.STATE_SUCCESS, GroupList.getGroupList(wbGroup, uid));
                    }

                    @Override
                    public void onError(GroupManageMvpView v, Throwable e) {
                        v.createGroupResult(TaskState.getFailState(e), null);
                    }
                });
    }

    public void updateGroup(String gid, String name, String description){
        final UrlBuilder ub = new UrlBuilder();
        final String uid = UserUtil.getUid();
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("list_id", gid);
        ub.addParameter("name", name);
        if(description == null){
            description = "";
        }
        ub.addParameter("description", description);
        RetrofitManager
                .getWBService()
                .updateGroup(ub.getParameters())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSubscriber<WBGroup>() {
                    @Override
                    public void onNext(GroupManageMvpView v, WBGroup wbGroup) {
                        v.updateGroupResult(TaskState.STATE_SUCCESS, GroupList.getGroupList(wbGroup, uid));
                    }

                    @Override
                    public void onError(GroupManageMvpView v, Throwable e) {
                        v.updateGroupResult(TaskState.getFailState(e), null);
                    }
                });
    }

    public void deleteGroup(String gid){
        final UrlBuilder ub = new UrlBuilder();
        final String uid = UserUtil.getUid();
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("list_id", gid);
        RetrofitManager
                .getWBService()
                .destroyGroup(ub.getParameters())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSubscriber<WBGroup>() {
                    @Override
                    public void onNext(GroupManageMvpView v, WBGroup wbGroup) {
                        v.deleteGroupResult(TaskState.STATE_SUCCESS, GroupList.getGroupList(wbGroup, uid));
                    }

                    @Override
                    public void onError(GroupManageMvpView v, Throwable e) {
                        v.deleteGroupResult(TaskState.getFailState(e), null);
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
