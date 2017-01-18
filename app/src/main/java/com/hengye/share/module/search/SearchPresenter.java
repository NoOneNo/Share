package com.hengye.share.module.search;

import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.model.sina.WBUserInfos;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.rxjava.ObjectConverter;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.http.retrofit.api.WBService;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SearchPresenter extends RxPresenter<SearchContract.View> implements SearchContract.Presenter{

    public SearchPresenter(SearchContract.View mvpView) {
        super(mvpView);
    }

    @Override
    public void loadWBSearchContent(String content) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBSearchUserUrl());
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("q", content);
        ub.addParameter("sid", "o_weico");

        WBService service = RetrofitManager.getWBService();

        Single.zip(
                service.searchUser(ub.getParameters()),
                service.searchPublic(ub.getParameters()),
                ObjectConverter.getObjectConverter2())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<Object[]>() {
                    @Override
                    public void onSuccess(SearchContract.View view, Object[] objects) {
                        view.handleSearchUserData(UserInfo.getUserInfos((WBUserInfos) objects[0]));
                        view.handleSearchPublicData(Topic.getTopics((WBTopics) objects[1]));
                        view.loadSuccess();
                    }

                    @Override
                    public void onError(SearchContract.View view, Throwable e) {
                        ToastUtil.showToast(TaskState.toString(TaskState.getFailState(e)));
                        view.loadFail();
                    }
                });
    }
}
