package com.hengye.share.module.hottopic;

import com.hengye.share.model.HotSearch;
import com.hengye.share.model.sina.WBHotSearch;
import com.hengye.share.module.util.encapsulation.mvp.TaskPresenter;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.ArrayList;

import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class HotSearchPresenter extends TaskPresenter<HotSearchContract.View>
        implements HotSearchContract.Presenter {

    public HotSearchPresenter(HotSearchContract.View mvpView) {
        super(mvpView);
    }

    private boolean isLoading;

    @Override
    public void loadHotSearch() {
        if(isLoading){
            return;
        }
        isLoading = true;
        getMvpView().onTaskStart();
        RetrofitManager
                .getWBService()
                .listHotSearch()
                .flatMap(flatWBHotSearchs())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new TaskSingleObserver<ArrayList<HotSearch>>(){
                    @Override
                    public void onSuccess(HotSearchContract.View view, ArrayList<HotSearch> hotSearches) {
                        super.onSuccess(view, hotSearches);
                        view.onLoadHotSearchSuccess(hotSearches);
                        isLoading = false;
                    }

                    @Override
                    public void onError(HotSearchContract.View view, Throwable e) {
                        super.onError(view, e);
                        isLoading = false;
                    }
                });
    }

    Function<WBHotSearch, SingleSource<ArrayList<HotSearch>>> mFlatWBHotSearch;

    /**
     * 把微博实体转换成自定义的实体
     * @return
     */
    private  Function<WBHotSearch, SingleSource<ArrayList<HotSearch>>> flatWBHotSearchs() {
        if (mFlatWBHotSearch == null) {
            mFlatWBHotSearch = new Function<WBHotSearch, SingleSource<ArrayList<HotSearch>>>() {
                @Override
                public SingleSource<ArrayList<HotSearch>> apply(WBHotSearch wbHotSearch) {
                    return SingleHelper.justArrayList(HotSearch.listHotSearch(wbHotSearch));
                }
            };
        }
        return mFlatWBHotSearch;
    }
}
