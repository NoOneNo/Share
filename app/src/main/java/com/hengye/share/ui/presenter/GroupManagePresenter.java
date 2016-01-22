package com.hengye.share.ui.presenter;

import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.ui.mvpview.GroupManageMvpView;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GroupManagePresenter extends BasePresenter<GroupManageMvpView> {

    public GroupManagePresenter(GroupManageMvpView mvpView){
        super(mvpView);
    }

    public void loadGroupList(){

        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getToken());
        RetrofitManager
                .getWBService()
                .listGroups(ub.getParameters())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WBGroups>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WBGroups wbGroups) {

                        UserUtil.updateGroupList(wbGroups);
                        getMvpView().handleGroupList(wbGroups);
                    }
                });
    }

}
