package com.hengye.share.module.statusdetail;

import com.hengye.share.model.StatusComment;
import com.hengye.share.model.sina.WBStatusComments;
import com.hengye.share.model.sina.WBStatusReposts;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.rxjava.ObjectConverter;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.http.retrofit.api.WBService;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.Map;

import io.reactivex.Single;

public class StatusDetailPresenter extends RxPresenter<StatusDetailMvpView> {

    public StatusDetailPresenter(StatusDetailMvpView mvpView) {
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
        ub.addParameter("count", WBUtil.getWBStatusRequestCount());
        return ub.getParameters();
    }

    public void loadWBCommentAndRepost(String topicId, String id, final boolean isRefresh) {
        WBService service = RetrofitManager.getWBService();
        Map<String, String> params = getParameter(UserUtil.getToken(), topicId, id, isRefresh);
//        Map<String, String> repostParams = getParameter(UserUtil.getToken(), topicId, id, isRefresh);
        Map<String, String> repostParams = getParameter(UserUtil.getPriorToken(), topicId, id, isRefresh);

        Single.zip(
                service.listComment(params),
                service.listRepost(repostParams),
                ObjectConverter.getObjectConverter2())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<Object[]>() {
                    @Override
                    public void onError(StatusDetailMvpView v, Throwable e) {
                        v.onTaskComplete(isRefresh, false);
                    }

                    @Override
                    public void onSuccess(StatusDetailMvpView v, Object[] objects) {
                        WBStatusComments obj1 = (WBStatusComments) objects[0];
                        WBStatusReposts obj2 = (WBStatusReposts) objects[1];
                        v.handleCommentData(true, StatusComment.getCommentArrayList(obj1), isRefresh, obj1.getTotal_number());
                        v.handleCommentData(false, StatusComment.getCommentArrayList(obj2), isRefresh, obj2.getTotal_number());
                        v.onTaskComplete(isRefresh, true);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public void loadWBCommentOrRepost(String topicId, String id, final boolean isRefresh, final boolean isComment) {
        WBService service = RetrofitManager.getWBService();
        Map<String, String> params = getParameter(isComment ? UserUtil.getToken() : UserUtil.getPriorToken(), topicId, id, isRefresh);

        Single observable = isComment ? service.listComment(params) : service.listRepost(params);

        observable
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new BaseSingleObserver<Object>() {

                    @Override
                    public void onError(StatusDetailMvpView v, Throwable e) {
                        v.onTaskComplete(isRefresh, false);
                    }

                    @Override
                    public void onSuccess(StatusDetailMvpView v, Object o) {
                        v.handleCommentData(isComment, isComment
                                ? StatusComment.getCommentArrayList((WBStatusComments) o)
                                : StatusComment.getCommentArrayList((WBStatusReposts) o), isRefresh, 0);
                        v.onTaskComplete(isRefresh, true);
                    }

                });
    }
}
