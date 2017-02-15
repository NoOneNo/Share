package com.hengye.share.module.topicdetail;

import com.hengye.share.model.Topic;
import com.hengye.share.model.sina.WBAttitude;
import com.hengye.share.model.sina.WBResult;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.http.retrofit.api.WBService;
import com.hengye.share.util.http.retrofit.weibo.WBApiException;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;

import java.util.Map;

import io.reactivex.Single;
import io.reactivex.functions.Function;

public class StatusActonPresenter extends RxPresenter<StatusActionContract.View>
        implements StatusActionContract.Presenter {

    public StatusActonPresenter(StatusActionContract.View mvpView) {
        super(mvpView);
    }

    @Override
    public void likeStatus(final Topic topic) {

        if (topic == null) {
            return;
        }

        final boolean liked = !topic.isLiked();
        topic.updateLiked(liked);
        getMvpView().onLikeStatusStart(topic);


        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("id", topic.getId());
        ub.addParameter("source", ThirdPartyUtils.getAppKeyForWeico());
        ub.addParameter("access_token", UserUtil.getPriorToken());
        if (liked) {
            ub.addParameter("attitude", "smile");
        }
        Map<String, String> params = ub.getParameters();

        WBService wbService = RetrofitManager.getWBService();

        Single<WBResult> single = liked ?
                wbService
                        .createStatusLike(params)
                        .flatMap(flatWBAttitude()) :
                wbService.destroyStatusLike(params);

        single
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<WBResult>() {
                    @Override
                    public void onSuccess(StatusActionContract.View view, WBResult wbResult) {

                        int taskState;
                        if (wbResult.isSuccess()) {
                            taskState = TaskState.STATE_SUCCESS;
                        } else {
                            taskState = TaskState.STATE_FAIL_BY_SERVER;
                            topic.updateLiked(!liked);
                        }

                        view.onLikeStatusComplete(topic, taskState);
                    }

                    @Override
                    public void onError(StatusActionContract.View view, Throwable e) {
                        topic.updateLiked(!liked);
                        view.onLikeStatusComplete(topic, TaskState.getFailState(e));
                    }
                });
    }

    @Override
    public void collectStatus(final Topic topic) {

        if (topic == null) {
            return;
        }

        final boolean favorited = !topic.isFavorited();
        topic.setFavorited(favorited);
        getMvpView().onCollectStatusStart(topic);

        final UrlBuilder ub;
        if (favorited) {
            ub = new UrlBuilder(UrlFactory.getInstance().getWBDestroyFavoritesUrl());
        } else {
            ub = new UrlBuilder(UrlFactory.getInstance().getWBCreateFavoritesUrl());
        }

        ub.addParameter("access_token", UserUtil.getToken());
        ub.addParameter("id", topic.getId());
        Map<String, String> params = ub.getParameters();

        WBService wbService = RetrofitManager.getWBService();

        Single<WBTopic> single = favorited ?
                wbService.createStatusFavorited(params) :
                wbService.destroyStatusFavorited(params);

        single
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<WBTopic>() {
                    @Override
                    public void onSuccess(StatusActionContract.View view, WBTopic wbTopic) {

                        int taskState;
                        if (wbTopic != null) {
                            taskState = TaskState.STATE_SUCCESS;
                        } else {
                            taskState = TaskState.STATE_FAIL_BY_SERVER;
                            topic.setFavorited(!favorited);
                        }

                        view.onCollectStatusComplete(topic, taskState);
                    }

                    @Override
                    public void onError(StatusActionContract.View view, Throwable e) {
                        if(e instanceof WBApiException){
                            WBApiException wbApiException = (WBApiException) e;
                            if(wbApiException.getErrorCode() == 20704){
                                //已经收藏
                                topic.setFavorited(true);
                            }else if(wbApiException.getErrorCode() == 20705){
                                //还没收藏
                                topic.setFavorited(false);
                            }else{
                                topic.setFavorited(!favorited);
                            }
                        }else{
                            topic.setFavorited(!favorited);
                        }

                        view.onCollectStatusComplete(topic, TaskState.getFailState(e));
                    }
                });
    }

    private Function<WBAttitude, Single<WBResult>> mFlatWBAttitude;

    private Function<WBAttitude, Single<WBResult>> flatWBAttitude() {
        if (mFlatWBAttitude == null) {
            mFlatWBAttitude = new Function<WBAttitude, Single<WBResult>>() {
                @Override
                public Single<WBResult> apply(WBAttitude wbTopicComments) throws Exception {
                    WBResult wbResult = new WBResult();
                    if (wbTopicComments != null) {
                        wbResult.setResult(true);
                    }
                    return Single
                            .just(wbResult);
                }
            };
        }
        return mFlatWBAttitude;
    }
}
