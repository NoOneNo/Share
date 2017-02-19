package com.hengye.share.module.hottopic;

import com.hengye.share.model.HotTopic;
import com.hengye.share.model.Status;
import com.hengye.share.model.sina.WBCards;
import com.hengye.share.model.sina.WBHotTopic;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.http.retrofit.api.WBService;
import com.hengye.share.util.rxjava.ObjectConverter;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

public class HotTopicAndStatusPresenter extends ListDataPresenter<Status, HotTopicAndStatusContract.View>
        implements HotTopicAndStatusContract.Presenter {

    public HotTopicAndStatusPresenter(HotTopicAndStatusContract.View mvpView) {
        super(mvpView);
    }

    @Override
    public void loadWBTopic(int id, boolean isRefresh) {
        getMvpView().onTaskStart();

        WBService wbService = RetrofitManager.getWBService();
        if(!isRefresh) {
            wbService
                    .listHotStatus(getWBAHotTopicParameter(id, false))
                    .flatMap(flatWBTopics())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new ListDataSingleObserver(false));
        }else{
            Single.zip(
                    wbService
                            .listHotTopic()
                            .flatMap(flatWBHotTopics())
                            .onErrorReturnItem(new ArrayList<HotTopic>()),
                    wbService
                            .listHotStatus(getWBAHotTopicParameter(id, true))
                            .flatMap(flatWBTopics()),
                            ObjectConverter.getObjectConverter2())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new ListTaskSingleObserver<Object[]>(true){
                        @Override
                        public void onSuccess(HotTopicAndStatusContract.View view, Object[] objects) {
                            super.onSuccess(view, objects);
                            ArrayList<HotTopic> obj1 = (ArrayList<HotTopic>) objects[0];
                            ArrayList<Status> obj2 = (ArrayList<Status>) objects[1];
                            view.onLoadHotTopic(obj1);
                            view.onLoadListData(isRefresh, obj2);
                        }

                        @Override
                        public void onError(HotTopicAndStatusContract.View view, Throwable e) {
                            super.onError(view, e);
                        }
                    });
        }
    }

    private Map<String, String> getWBAHotTopicParameter(int id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder();
        if (!isRefresh) {
            ub.addParameter("since_id", id);
        }
        return ub.getParameters();
    }


    Function<WBCards, SingleSource<ArrayList<Status>>> mFlatWBTopic;

    /**
     * 把微博实体转换成自定义的实体
     * @return
     */
    private  Function<WBCards, SingleSource<ArrayList<Status>>> flatWBTopics() {
        if (mFlatWBTopic == null) {
            mFlatWBTopic = new Function<WBCards, SingleSource<ArrayList<Status>>>() {
                @Override
                public SingleSource<ArrayList<Status>> apply(WBCards wbCards) {
                    return SingleHelper.justArrayList(Status.getStatuses(WBCards.listWBStatuses(wbCards)));
                }
            };
        }
        return mFlatWBTopic;
    }


    Function<WBHotTopic, SingleSource<ArrayList<HotTopic>>> mFlatWBHotTopic;

    /**
     * 把微博实体转换成自定义的实体
     * @return
     */
    private  Function<WBHotTopic, SingleSource<ArrayList<HotTopic>>> flatWBHotTopics() {
        if (mFlatWBHotTopic == null) {
            mFlatWBHotTopic = new Function<WBHotTopic, SingleSource<ArrayList<HotTopic>>>() {
                @Override
                public SingleSource<ArrayList<HotTopic>> apply(WBHotTopic wbHotTopic) {
                    return SingleHelper.justArrayList(HotTopic.listHotTopics(wbHotTopic));
                }
            };
        }
        return mFlatWBHotTopic;
    }

}
