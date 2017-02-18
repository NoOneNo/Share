package com.hengye.share.module.topicdetail;

import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicComments;
import com.hengye.share.model.sina.WBResult;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicReposts;
import com.hengye.share.module.topic.TopicRxUtil;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.ListTaskPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.http.retrofit.api.WBService;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class TopicCommentPresenter extends ListTaskPresenter<TopicCommentContract.View> implements TopicCommentContract.Presenter{

    private boolean mIsLikeMode;

    public TopicCommentPresenter(TopicCommentContract.View mvpView, boolean isLikeMode) {
        super(mvpView);
        mIsLikeMode = isLikeMode;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadWBCommentOrRepost(String topicId, String id, final boolean isRefresh, final boolean isComment) {
        if (isRefresh) {
            getMvpView().onTaskStart();
        }

        getComment(topicId, id, isRefresh, isComment)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getTopicCommentsSubscriber(isRefresh));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadWBHotComment(boolean isRefresh, String topicId, int page, int count) {
        if (isRefresh) {
            getMvpView().onTaskStart();
        }

        getHotComment(topicId, page, count)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getTopicCommentsSubscriber(isRefresh));
    }

    private Map<String, String> getParameter(String token, String topicId, String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", token);
        ub.addParameter("id", topicId);
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }
        ub.addParameter("count", WBUtil.getWBTopicRequestCount());
        return ub.getParameters();
    }

    private Single<TopicComments> getComment(final String topicId, String id, final boolean isRefresh, final boolean isComment) {
        final WBService service = RetrofitManager.getWBService();
        if (!isComment) {
            //转发
            return service
                    .listRepost(getParameter(UserUtil.getPriorToken(), topicId, id, isRefresh))
                    .flatMap(flatWBTopicReposts());
        } else {
            //评论
            if (!mIsLikeMode) {
                //如果没有高级授权，没有点赞
                return service
                        .listComment(getParameter(UserUtil.getToken(), topicId, id, isRefresh))
                        .flatMap(flatWBTopicComments());
            } else {
                Map<String, String> params = getParameter(UserUtil.getPriorToken(), topicId, id, isRefresh);
                params.put("source", ThirdPartyUtils.getAppKeyForWeico());
                Single<TopicComments> topicCommentsObservable = service.listCommentWithLike(params).flatMap(flatWBTopicComments());

                if (!isRefresh) {
                    return topicCommentsObservable;
                } else {
                    return topicCommentsObservable
                            .flatMap(new Function<TopicComments, SingleSource<TopicComments>>() {
                                @Override
                                public SingleSource<TopicComments> apply(final TopicComments topicComments) throws Exception {
                                    if (topicComments != null && topicComments.getTotalNumber() > WBUtil.getWBTopicRequestCount()) {
                                        //尝试获取热门评论
                                        return getHotComment(topicId, 1, 10)
                                                .flatMap(new Function<TopicComments, SingleSource<TopicComments>>() {
                                                    @Override
                                                    public SingleSource<TopicComments> apply(TopicComments topicHotComments) throws Exception {
                                                        if (topicHotComments != null && !CommonUtil.isEmpty(topicHotComments.getComments())) {
                                                            if (topicComments.getComments() != null) {
                                                                topicComments.getComments().add(0, TopicComments.getTopicHotCommentLabel());
                                                                topicComments.getComments().addAll(0, topicHotComments.getComments());
                                                                topicComments.setHotTotalNumber(topicHotComments.getHotTotalNumber());
                                                            }
                                                        }
                                                        return Single.just(topicComments);
                                                    }
                                                })
                                                .onErrorReturnItem(topicComments);
                                    }
                                    return Single.just(topicComments == null ? new TopicComments() : topicComments);
                                }
                            });
                }


            }
        }
    }

    /**
     * 获取热门评论
     *
     * @param topicId
     * @param page
     * @param count
     * @return
     */
    private Single<TopicComments> getHotComment(String topicId, int page, int count) {
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("source", ThirdPartyUtils.getAppKeyForWeico());
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("id", topicId);
        ub.addParameter("page", page);
        ub.addParameter("count", count);
        return RetrofitManager
                .getWBService()
                .listHotCommentWithLike(ub.getParameters())
                .flatMap(flatWBTopicComments());

    }

    private SingleObserver<TopicComments> getTopicCommentsSubscriber(boolean isRefresh) {
        return new ListTaskSingleObserver<TopicComments>(isRefresh){
            @Override
            public void onSuccess(TopicCommentContract.View view, TopicComments topicComments) {
                super.onSuccess(view, topicComments);
                view.onLoadTopicComments(topicComments);
                view.onLoadListData(isRefresh, topicComments.getComments());
            }

            @Override
            public void onError(TopicCommentContract.View view, Throwable e) {
                super.onError(view, e);
            }
        };
    }

    Function<WBTopicReposts, SingleSource<TopicComments>> mFlatWBTopicReposts;

    private Function<WBTopicReposts, SingleSource<TopicComments>> flatWBTopicReposts() {
        if (mFlatWBTopicReposts == null) {
            mFlatWBTopicReposts = new Function<WBTopicReposts, SingleSource<TopicComments>>() {
                @Override
                public SingleSource<TopicComments> apply(WBTopicReposts wbTopicReposts) throws Exception {
                    return Single
                            .just(TopicComment.getComments(wbTopicReposts))
                            .flatMap(TopicRxUtil.flatTopicCommentsShortUrl());
                }
            };
        }
        return mFlatWBTopicReposts;
    }

    Function<WBTopicComments, SingleSource<TopicComments>> mFlatWBTopicComments;

    private Function<WBTopicComments, SingleSource<TopicComments>> flatWBTopicComments() {
        if (mFlatWBTopicComments == null) {
            mFlatWBTopicComments = new Function<WBTopicComments, SingleSource<TopicComments>>() {
                @Override
                public SingleSource<TopicComments> apply(WBTopicComments wbTopicComments) throws Exception {
                    return Single
                            .just(TopicComment.getComments(wbTopicComments))
                            .flatMap(TopicRxUtil.flatTopicCommentsShortUrl());
                }
            };
        }
        return mFlatWBTopicComments;
    }

    @Override
    public void likeComment(final TopicComment topicComment) {

        if (topicComment == null) {
            return;
        }

        final boolean isLike = !topicComment.isLiked();

        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("source", ThirdPartyUtils.getAppKeyForWeico());
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("object_id", topicComment.getId());
        ub.addParameter("object_type", "comment");
        Map<String, String> params = ub.getParameters();

        WBService service = RetrofitManager.getWBService();
        Single<WBResult> observable = isLike ? service.createCommentLike(params) : service.destroyCommentLike(params);

        observable
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<WBResult>() {
                    @Override
                    public void onSuccess(TopicCommentContract.View topicCommentMvpView, WBResult result) {
                        int taskState;

                        if (result != null && result.isSuccess()) {
                            taskState = TaskState.STATE_SUCCESS;
                            topicComment.setLiked(isLike);
                        } else {
                            taskState = TaskState.STATE_FAIL_BY_SERVER;
                        }
                        topicCommentMvpView.onTopicCommentLike(topicComment, taskState);
                    }

                    @Override
                    public void onError(TopicCommentContract.View topicCommentMvpView, Throwable e) {
                        topicCommentMvpView.onTopicCommentLike(topicComment, TaskState.getFailState(e));
                    }
                });
    }
}
