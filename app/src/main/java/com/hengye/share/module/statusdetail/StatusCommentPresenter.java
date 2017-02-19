package com.hengye.share.module.statusdetail;

import com.hengye.share.model.StatusComment;
import com.hengye.share.model.StatusComments;
import com.hengye.share.model.sina.WBResult;
import com.hengye.share.model.sina.WBStatusComments;
import com.hengye.share.model.sina.WBStatusReposts;
import com.hengye.share.module.status.StatusRxUtil;
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

public class StatusCommentPresenter extends ListTaskPresenter<StatusCommentContract.View> implements StatusCommentContract.Presenter{

    private boolean mIsLikeMode;

    public StatusCommentPresenter(StatusCommentContract.View mvpView, boolean isLikeMode) {
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
                .subscribe(getStatusCommentsSubscriber(isRefresh));
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
                .subscribe(getStatusCommentsSubscriber(isRefresh));
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
        ub.addParameter("count", WBUtil.getWBStatusRequestCount());
        return ub.getParameters();
    }

    private Single<StatusComments> getComment(final String topicId, String id, final boolean isRefresh, final boolean isComment) {
        final WBService service = RetrofitManager.getWBService();
        if (!isComment) {
            //转发
            return service
                    .listRepost(getParameter(UserUtil.getPriorToken(), topicId, id, isRefresh))
                    .flatMap(flatWBStatusReposts());
        } else {
            //评论
            if (!mIsLikeMode) {
                //如果没有高级授权，没有点赞
                return service
                        .listComment(getParameter(UserUtil.getToken(), topicId, id, isRefresh))
                        .flatMap(flatWBStatusComments());
            } else {
                Map<String, String> params = getParameter(UserUtil.getPriorToken(), topicId, id, isRefresh);
                params.put("source", ThirdPartyUtils.getAppKeyForWeico());
                Single<StatusComments> topicCommentsObservable = service.listCommentWithLike(params).flatMap(flatWBStatusComments());

                if (!isRefresh) {
                    return topicCommentsObservable;
                } else {
                    return topicCommentsObservable
                            .flatMap(new Function<StatusComments, SingleSource<StatusComments>>() {
                                @Override
                                public SingleSource<StatusComments> apply(final StatusComments topicComments) throws Exception {
                                    if (topicComments != null && topicComments.getTotalNumber() > WBUtil.getWBStatusRequestCount()) {
                                        //尝试获取热门评论
                                        return getHotComment(topicId, 1, 10)
                                                .flatMap(new Function<StatusComments, SingleSource<StatusComments>>() {
                                                    @Override
                                                    public SingleSource<StatusComments> apply(StatusComments topicHotComments) throws Exception {
                                                        if (topicHotComments != null && !CommonUtil.isEmpty(topicHotComments.getComments())) {
                                                            if (topicComments.getComments() != null) {
                                                                topicComments.getComments().add(0, StatusComments.getStatusHotCommentLabel());
                                                                topicComments.getComments().addAll(0, topicHotComments.getComments());
                                                                topicComments.setHotTotalNumber(topicHotComments.getHotTotalNumber());
                                                            }
                                                        }
                                                        return Single.just(topicComments);
                                                    }
                                                })
                                                .onErrorReturnItem(topicComments);
                                    }
                                    return Single.just(topicComments == null ? new StatusComments() : topicComments);
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
    private Single<StatusComments> getHotComment(String topicId, int page, int count) {
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("source", ThirdPartyUtils.getAppKeyForWeico());
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("id", topicId);
        ub.addParameter("page", page);
        ub.addParameter("count", count);
        return RetrofitManager
                .getWBService()
                .listHotCommentWithLike(ub.getParameters())
                .flatMap(flatWBStatusComments());

    }

    private SingleObserver<StatusComments> getStatusCommentsSubscriber(boolean isRefresh) {
        return new ListTaskSingleObserver<StatusComments>(isRefresh){
            @Override
            public void onSuccess(StatusCommentContract.View view, StatusComments topicComments) {
                super.onSuccess(view, topicComments);
                view.onLoadStatusComments(topicComments);
                view.onLoadListData(isRefresh, topicComments.getComments());
            }

            @Override
            public void onError(StatusCommentContract.View view, Throwable e) {
                super.onError(view, e);
            }
        };
    }

    Function<WBStatusReposts, SingleSource<StatusComments>> mFlatWBStatusReposts;

    private Function<WBStatusReposts, SingleSource<StatusComments>> flatWBStatusReposts() {
        if (mFlatWBStatusReposts == null) {
            mFlatWBStatusReposts = new Function<WBStatusReposts, SingleSource<StatusComments>>() {
                @Override
                public SingleSource<StatusComments> apply(WBStatusReposts wbStatusReposts) throws Exception {
                    return Single
                            .just(StatusComment.getComments(wbStatusReposts))
                            .flatMap(StatusRxUtil.flatStatusCommentsShortUrl());
                }
            };
        }
        return mFlatWBStatusReposts;
    }

    Function<WBStatusComments, SingleSource<StatusComments>> mFlatWBStatusComments;

    private Function<WBStatusComments, SingleSource<StatusComments>> flatWBStatusComments() {
        if (mFlatWBStatusComments == null) {
            mFlatWBStatusComments = new Function<WBStatusComments, SingleSource<StatusComments>>() {
                @Override
                public SingleSource<StatusComments> apply(WBStatusComments wbStatusComments) throws Exception {
                    return Single
                            .just(StatusComment.getComments(wbStatusComments))
                            .flatMap(StatusRxUtil.flatStatusCommentsShortUrl());
                }
            };
        }
        return mFlatWBStatusComments;
    }

    @Override
    public void likeComment(final StatusComment statusComment) {

        if (statusComment == null) {
            return;
        }

        final boolean isLike = !statusComment.isLiked();

        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("source", ThirdPartyUtils.getAppKeyForWeico());
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("object_id", statusComment.getId());
        ub.addParameter("object_type", "comment");
        Map<String, String> params = ub.getParameters();

        WBService service = RetrofitManager.getWBService();
        Single<WBResult> observable = isLike ? service.createCommentLike(params) : service.destroyCommentLike(params);

        observable
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<WBResult>() {
                    @Override
                    public void onSuccess(StatusCommentContract.View topicCommentMvpView, WBResult result) {
                        int taskState;

                        if (result != null && result.isSuccess()) {
                            taskState = TaskState.STATE_SUCCESS;
                            statusComment.setLiked(isLike);
                        } else {
                            taskState = TaskState.STATE_FAIL_BY_SERVER;
                        }
                        topicCommentMvpView.onStatusCommentLike(statusComment, taskState);
                    }

                    @Override
                    public void onError(StatusCommentContract.View topicCommentMvpView, Throwable e) {
                        topicCommentMvpView.onStatusCommentLike(statusComment, TaskState.getFailState(e));
                    }
                });
    }
}
