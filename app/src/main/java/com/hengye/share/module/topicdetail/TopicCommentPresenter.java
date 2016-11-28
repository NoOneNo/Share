package com.hengye.share.module.topicdetail;

import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicComments;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicReposts;
import com.hengye.share.module.util.encapsulation.mvp.TaskPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.retrofit.api.WBService;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

public class TopicCommentPresenter extends TaskPresenter<TopicCommentMvpView> {

    private boolean mIsLikeMode;

    public TopicCommentPresenter(TopicCommentMvpView mvpView, boolean isLikeMode) {
        super(mvpView);
        mIsLikeMode = isLikeMode;
    }

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

    public Map<String, String> getParameter(String token, String topicId, String id, final boolean isRefresh) {
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

    private Observer<TopicComments> getTopicCommentsSubscriber(boolean isRefresh) {
        return new TaskSubscriber<TopicComments>(isRefresh) {
            @Override
            public void onNext(TopicCommentMvpView mvpView, TopicComments list) {
                mvpView.updateTotalCount(list.getTotalNumber());
                mvpView.onLoadListData(isRefresh, list.getComments());
            }
        };
    }

    private Observable<TopicComments> getComment(final String topicId, String id, final boolean isRefresh, final boolean isComment) {
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
                params.put("source", ThirdPartyUtils.getAppKeyForWeibo(ThirdPartyUtils.WeiboApp.WEICO));
                Observable<TopicComments> topicCommentsObservable = service.listCommentWithLike(params).flatMap(flatWBTopicComments());

                if (!isRefresh) {
                    return topicCommentsObservable;
                } else {
                    return topicCommentsObservable
                            .flatMap(new Function<TopicComments, ObservableSource<TopicComments>>() {
                                @Override
                                public ObservableSource<TopicComments> apply(final TopicComments topicComments) throws Exception {
                                    if (topicComments != null && topicComments.getTotalNumber() > WBUtil.getWBTopicRequestCount()) {
                                        //尝试获取热门评论
                                        final UrlBuilder ub = new UrlBuilder();
                                        ub.addParameter("source", ThirdPartyUtils.getAppKeyForWeibo(ThirdPartyUtils.WeiboApp.WEICO));
                                        ub.addParameter("access_token", UserUtil.getPriorToken());
                                        ub.addParameter("id", topicId);
                                        ub.addParameter("page", 1);
                                        ub.addParameter("count", 10);
                                        return service
                                                .listHotCommentWithLike(ub.getParameters())
                                                .flatMap(flatWBTopicComments())
                                                .flatMap(new Function<TopicComments, ObservableSource<TopicComments>>() {
                                                    @Override
                                                    public ObservableSource<TopicComments> apply(TopicComments topicHotComments) throws Exception {
                                                        if (topicHotComments != null && !CommonUtil.isEmpty(topicHotComments.getComments())) {
                                                            if (topicComments.getComments() != null) {
                                                                topicComments.getComments().add(0, TopicComments.getTopicHotCommentLabel());
                                                                topicComments.getComments().addAll(0, topicHotComments.getComments());
                                                                topicComments.setHotTotalNumber(topicHotComments.getHotTotalNumber());
                                                            }
                                                        }
                                                        return ObservableHelper.just(topicComments);
                                                    }
                                                });
                                    }
                                    return ObservableHelper.just(topicComments);
                                }
                            });
                }


            }
        }
    }

    Function<WBTopicReposts, ObservableSource<TopicComments>> mFlatWBTopicReposts;

    private Function<WBTopicReposts, ObservableSource<TopicComments>> flatWBTopicReposts() {
        if (mFlatWBTopicReposts == null) {
            mFlatWBTopicReposts = new Function<WBTopicReposts, ObservableSource<TopicComments>>() {
                @Override
                public ObservableSource<TopicComments> apply(WBTopicReposts wbTopicReposts) throws Exception {
                    return ObservableHelper.just(TopicComment.getComments(wbTopicReposts));
                }
            };
        }
        return mFlatWBTopicReposts;
    }

    Function<WBTopicComments, ObservableSource<TopicComments>> mFlatWBTopicComments;

    private Function<WBTopicComments, ObservableSource<TopicComments>> flatWBTopicComments() {
        if (mFlatWBTopicComments == null) {
            mFlatWBTopicComments = new Function<WBTopicComments, ObservableSource<TopicComments>>() {
                @Override
                public ObservableSource<TopicComments> apply(WBTopicComments wbTopicComments) throws Exception {
                    return ObservableHelper.just(TopicComment.getComments(wbTopicComments));
                }
            };
        }
        return mFlatWBTopicComments;
    }

//    @SuppressWarnings("unchecked")
//    public void loadWBCommentOrRepost(String topicId, String id, final boolean isRefresh, final boolean isComment) {
//        if (isRefresh) {
//            getMvpView().onTaskStart();
//        }
//
//        WBService service = RetrofitManager.getWBService();
//        Map<String, String> params = getParameter(isComment ? UserUtil.getToken() : UserUtil.getPriorToken(), topicId, id, isRefresh);
//
//        Observable observable = isComment ? service.listComment(params) : service.listRepost(params);
//
//        observable
//                .flatMap(new Function() {
//                    @Override
//                    public Observable<TopicComments> apply(Object o) {
//                        TopicComments result;
//                        if (isComment) {
//                            result = TopicComment.getComments((WBTopicComments) o);
//                        } else {
//                            result = TopicComment.getComments((WBTopicReposts) o);
//                        }
//                        return ObservableHelper.just(result);
//                    }
//                })
//                .subscribeOn(SchedulerProvider.io())
//                .observeOn(SchedulerProvider.ui())
//                .subscribe(getTopicCommentsSubscriber(isRefresh));
//    }
}
