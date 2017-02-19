package com.hengye.share.module.search;

import com.hengye.share.model.Status;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBStatuses;
import com.hengye.share.model.sina.WBUserInfos;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.http.retrofit.api.WBService;
import com.hengye.share.util.rxjava.ObjectConverter;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.Map;

import io.reactivex.Single;

public class SearchPresenter extends ListDataPresenter<Status, SearchContract.View> implements SearchContract.Presenter{

    public SearchPresenter(SearchContract.View mvpView) {
        super(mvpView);
    }

    @Override
    public void searchWBContent(String content, boolean isRefresh, int page, int count) {

        getMvpView().onTaskStart();

        WBService service = RetrofitManager.getWBService();
        Single.zip(
                service.searchUser(getSearchUserParams(content, count)),
                service.searchPublic(getSearchStatusParams(content, page, count)),
                ObjectConverter.getObjectConverter2())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new ListTaskSingleObserver<Object[]>(isRefresh){
                    @Override
                    public void onSuccess(SearchContract.View view, Object[] objects) {
                        super.onSuccess(view, objects);
                        view.onLoadSearchUsers(UserInfo.getUserInfos((WBUserInfos) objects[0]));
                        view.onLoadListData(isRefresh, Status.getStatuses((WBStatuses) objects[1]));
                    }
                });
    }


    public Map<String, String> getSearchUserParams(String content, int count){
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("sid", "o_weico");
        ub.addParameter("q", content);
        ub.addParameter("count", count);
        return ub.getParameters();
    }

    public Map<String, String> getSearchStatusParams(String content, int page, int count){
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("sid", "o_weico");
        ub.addParameter("q", content);
        ub.addParameter("page", page);
        ub.addParameter("count", count);
        return ub.getParameters();
    }
}
