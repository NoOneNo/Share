package com.hengye.share.module.topicdetail;

import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicComments;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBAttitudes;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.module.topic.TopicRxUtil;
import com.hengye.share.module.util.encapsulation.mvp.ListTaskPresenter;
import com.hengye.share.module.util.encapsulation.mvp.RxPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.ObjectConverter;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class StatusLikePresenter extends ListTaskPresenter<StatusLikeContract.View>
        implements StatusLikeContract.Presenter {

    public StatusLikePresenter(StatusLikeContract.View mvpView) {
        super(mvpView);
    }

    @Override
    public void listStatusLike(boolean isRefresh, String statusId, int page, int count) {

        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("source", ThirdPartyUtils.getAppKeyForWeico());
        ub.addParameter("id", statusId);
        ub.addParameter("page", page);
        ub.addParameter("count", count);

        RetrofitManager
                .getWBService()
                .listStatusLike(ub.getParameters())
                .flatMap(flatWBAttitudes())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new ListTaskSingleObserver<Object[]>(isRefresh){
                    @Override
                    public void onSuccess(StatusLikeContract.View view, Object[] objects) {
                        super.onSuccess(view, objects);
                        ArrayList<UserInfo> userInfos = (ArrayList<UserInfo>) objects[0];
                        long totalNumber = (long) objects[1];
                        view.onLoadStatusLikeCount(totalNumber);
                        view.onLoadListData(isRefresh, userInfos);
                    }
                });
    }

    Function<WBAttitudes, SingleSource<Object[]>> mFlatWBAttitudes;

    private Function<WBAttitudes, SingleSource<Object[]>> flatWBAttitudes() {
        if (mFlatWBAttitudes == null) {
            mFlatWBAttitudes = new Function<WBAttitudes, SingleSource<Object[]>>() {
                @Override
                public SingleSource<Object[]> apply(WBAttitudes wbAttitudes) throws Exception {
                    return Single
                            .just(ObjectConverter.
                                    get(UserInfo.getUserInfos(wbAttitudes.getUsers()),
                                    wbAttitudes.getTotal_number()));
                }
            };
        }
        return mFlatWBAttitudes;
    }
}
