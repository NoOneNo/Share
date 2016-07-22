package com.hengye.share.ui.presenter;

import com.hengye.share.model.TopicComment;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicReposts;
import com.hengye.share.ui.mvpview.TopicDetailMvpView;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.rxjava.ObjectConverter;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.retrofit.WBService;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TopicDetailPresenter extends BasePresenter<TopicDetailMvpView> {

    public TopicDetailPresenter(TopicDetailMvpView mvpView) {
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

    public void loadWBCommentAndRepost(String topicId, String id, final boolean isRefresh) {
        WBService service = RetrofitManager.getWBService();
        Map<String, String> params = getParameter(UserUtil.getToken(), topicId, id, isRefresh);
        Map<String, String> repostParams = getParameter(UserUtil.getAdToken(), topicId, id, isRefresh);
        Observable.zip(
                service.listComment(params),
                service.listRepost(repostParams),
                ObjectConverter.getObjectConverter2())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Object[]>() {
                    @Override
                    public void handleViewOnFail(TopicDetailMvpView v, Throwable e) {
                        v.loadFail(isRefresh);
                    }

                    @Override
                    public void handleViewOnSuccess(TopicDetailMvpView v, Object[] objects) {
                        WBTopicComments obj1 = (WBTopicComments) objects[0];
                        WBTopicReposts obj2 = (WBTopicReposts) objects[1];
                        v.handleCommentData(true, TopicComment.getComments(obj1), isRefresh, obj1.getTotal_number());
                        v.handleCommentData(false, TopicComment.getComments(obj2), isRefresh, obj2.getTotal_number());
                        v.loadSuccess(isRefresh);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public void loadWBCommentOrRepost(String topicId, String id, final boolean isRefresh, final boolean isComment) {
        WBService service = RetrofitManager.getWBService();
        Map<String, String> params = getParameter(isComment ? UserUtil.getToken() : UserUtil.getAdToken(), topicId, id, isRefresh);

        Observable observable = isComment ? service.listComment(params) : service.listRepost(params);

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<Object>() {

                    @Override
                    public void handleViewOnFail(TopicDetailMvpView v, Throwable e) {
                        v.loadFail(isRefresh);
                    }

                    @Override
                    public void handleViewOnSuccess(TopicDetailMvpView v, Object o) {
                        if (isComment) {
                            v.handleCommentData(isComment, TopicComment.getComments((WBTopicComments) o), isRefresh, 0);
                        } else {
                            v.handleCommentData(isComment, TopicComment.getComments((WBTopicReposts) o), isRefresh, 0);
                        }
                       v.loadSuccess(isRefresh);
                    }

                });
    }
}
