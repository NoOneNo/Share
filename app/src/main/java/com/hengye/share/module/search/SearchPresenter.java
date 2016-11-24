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
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.retrofit.api.WBService;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest;
import io.reactivex.subscribers.ResourceSubscriber;

public class SearchPresenter extends RxPresenter<SearchMvpView> {

    public SearchPresenter(SearchMvpView mvpView) {
        super(mvpView);
    }

    public void loadWBSearchContent(String content) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBSearchUserUrl());
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("q", content);
        ub.addParameter("sid", "o_weico");

        WBService service = RetrofitManager.getWBService();

        Observable.zip(
                service.searchUser(ub.getParameters()),
                service.searchPublic(ub.getParameters()),
                ObjectConverter.getObjectConverter2())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSubscriber<Object[]>() {

                    @Override
                    public void onError(SearchMvpView v, Throwable e) {
                        v.loadFail();
                        ToastUtil.showToast(TaskState.toTaskStateString(TaskState.getTaskFailState(e)));
                    }

                    @Override
                    public void onNext(SearchMvpView v, Object[] objects) {
                        v.handleSearchUserData(UserInfo.getUserInfos((WBUserInfos) objects[0]));
                        v.handleSearchPublicData(Topic.getTopics((WBTopics) objects[1]));
                        v.loadSuccess();
                    }
                });
    }
}
