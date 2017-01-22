package com.hengye.share.module.groupmanage;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.sina.WBGroup;
import com.hengye.share.module.groupmanage.data.GroupManageDataSource;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.ListTaskPresenter;
import com.hengye.share.module.util.encapsulation.mvp.TaskPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class GroupManagePresenter extends ListTaskPresenter<GroupManageContract.View> implements GroupManageContract.Presenter{

    @NonNull
    private final GroupManageDataSource mGroupManageRepository;

    public GroupManagePresenter(
            @NonNull GroupManageContract.View mvpView,
            @NonNull GroupManageDataSource groupManageRepository) {
        super(mvpView);

        mGroupManageRepository = groupManageRepository;
    }

    @Override
    public void loadGroupList() {
        mGroupManageRepository
                .getGroupList()
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new ListTaskSingleObserver<ArrayList<GroupList>>(true) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    public void onSuccess(GroupManageContract.View view, ArrayList<GroupList> groupLists) {
                        super.onSuccess(view, groupLists);
                        view.loadGroupList(true, groupLists);
                    }
                });
    }

    @Override
    public void refreshGroupList() {
        mGroupManageRepository
                .refreshGroupList()
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new ListTaskSingleObserver<ArrayList<GroupList>>(true) {
                    @Override
                    public void onSuccess(GroupManageContract.View view, ArrayList<GroupList> groupLists) {
                        super.onSuccess(view, groupLists);
                        view.loadGroupList(false, groupLists);
                    }
                });
    }

    @Override
    public void updateGroupOrder(final ArrayList<GroupList> data) {
        mGroupManageRepository
                .updateGroupList(data)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(GroupManageContract.View view, Boolean isSuccess) {
                        view.updateGroupOrderCallBack(TaskState.STATE_SUCCESS);
                    }

                    @Override
                    public void onError(GroupManageContract.View view, Throwable e) {
                        view.updateGroupOrderCallBack(TaskState.getFailState(e));
                    }
                });
    }

    @Override
    public void checkGroupOrderIsChange(@NonNull List<GroupList> groupLists) {
        SingleHelper
                .justArrayList(groupLists)
                .flatMap(new Function<ArrayList<GroupList>, SingleSource<Boolean>>() {
                    @Override
                    public SingleSource<Boolean> apply(ArrayList<GroupList> groupLists) throws Exception {
                        return Single.just(!groupLists.equals(UserUtil.queryGroupList(false)));
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(GroupManageContract.View view, Boolean isChange) {
                        view.checkGroupOrder(isChange);
                    }

                    @Override
                    public void onError(GroupManageContract.View view, Throwable e) {
                        view.checkGroupOrder(false);
                    }
                });
    }

    @Override
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
                .subscribe(new BaseSingleObserver<WBGroup>() {
                    @Override
                    public void onSuccess(GroupManageContract.View view, WBGroup wbGroup) {
                        view.createGroupResult(TaskState.STATE_SUCCESS, GroupList.getGroupList(wbGroup, uid));
                    }

                    @Override
                    public void onError(GroupManageContract.View view, Throwable e) {
                        view.createGroupResult(TaskState.getFailState(e), null);
                    }
                });
    }

    @Override
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
                .subscribe(new BaseSingleObserver<WBGroup>() {
                    @Override
                    public void onSuccess(GroupManageContract.View view, WBGroup wbGroup) {
                        view.updateGroupResult(TaskState.STATE_SUCCESS, GroupList.getGroupList(wbGroup, uid));
                    }

                    @Override
                    public void onError(GroupManageContract.View view, Throwable e) {
                        view.updateGroupResult(TaskState.getFailState(e), null);
                    }
                });
    }

    @Override
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
                .subscribe(new BaseSingleObserver<WBGroup>() {
                    @Override
                    public void onSuccess(GroupManageContract.View view, WBGroup wbGroup) {
                        view.deleteGroupResult(TaskState.STATE_SUCCESS, GroupList.getGroupList(wbGroup, uid));
                    }

                    @Override
                    public void onError(GroupManageContract.View view, Throwable e) {
                        view.deleteGroupResult(TaskState.getFailState(e), null);
                    }
                });
    }
}
