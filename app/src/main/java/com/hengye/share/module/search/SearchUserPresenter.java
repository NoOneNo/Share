package com.hengye.share.module.search;

import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBUserInfos;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class SearchUserPresenter extends ListDataPresenter<UserInfo, SearchUserContract.View> implements SearchUserContract.Presenter {

    public SearchUserPresenter(SearchUserContract.View mvpView) {
        super(mvpView);
    }

    @Override
    public void searchWBUser(String content, boolean isRefresh, int page, int count) {

        getMvpView().onTaskStart();
        RetrofitManager
                .getWBService()
                .searchUser(getSearchUserParams(content, page, count))
                .flatMap(flatWBUserInfos())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new ListDataSingleObserver(isRefresh));
    }


    public Map<String, String> getSearchUserParams(String content, int page, int count) {
        final UrlBuilder ub = new UrlBuilder();
        ub.addParameter("access_token", UserUtil.getPriorToken());
        ub.addParameter("sid", "o_weico");
        ub.addParameter("q", content);
        ub.addParameter("page", page);
        ub.addParameter("count", count);
        return ub.getParameters();
    }

    Function<WBUserInfos, SingleSource<ArrayList<UserInfo>>> mFlatWBUserInfos;

    private Function<WBUserInfos, SingleSource<ArrayList<UserInfo>>> flatWBUserInfos() {
        if (mFlatWBUserInfos == null) {
            mFlatWBUserInfos = new Function<WBUserInfos, SingleSource<ArrayList<UserInfo>>>() {
                @Override
                public SingleSource<ArrayList<UserInfo>> apply(WBUserInfos WBUserInfos) throws Exception {
                    return SingleHelper
                            .justArrayList((UserInfo.getUserInfos(WBUserInfos.getUsers())));
                }
            };
        }
        return mFlatWBUserInfos;
    }
}
