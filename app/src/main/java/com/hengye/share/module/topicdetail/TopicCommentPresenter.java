package com.hengye.share.module.topicdetail;

import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicComments;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicReposts;
import com.hengye.share.module.util.encapsulation.mvp.TaskPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.retrofit.api.WBService;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.WBUtil;

import io.reactivex.Observer;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class TopicCommentPresenter extends TaskPresenter<TopicCommentMvpView> {

    public TopicCommentPresenter(TopicCommentMvpView mvpView) {
        super(mvpView);
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

    @SuppressWarnings("unchecked")
    public void loadWBCommentOrRepost(String topicId, String id, final boolean isRefresh, final boolean isComment) {
        if(isRefresh) {
            getMvpView().onTaskStart();
        }

        WBService service = RetrofitManager.getWBService();
        Map<String, String> params = getParameter(isComment ? UserUtil.getToken() : UserUtil.getPriorToken(), topicId, id, isRefresh);

        Observable observable = isComment ? service.listComment(params) : service.listRepost(params);

        observable
                .flatMap(new Function() {
                    @Override
                    public Observable<TopicComments> apply(Object o) {
                        TopicComments result;
                        if (isComment) {
                            result = TopicComment.getComments((WBTopicComments) o);
                        } else {
                            result = TopicComment.getComments((WBTopicReposts) o);
                        }
                        return ObservableHelper.just(result);
                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(getTopicCommentsSubscriber(isRefresh));
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
}
