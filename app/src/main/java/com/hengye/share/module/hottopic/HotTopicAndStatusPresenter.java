package com.hengye.share.module.hottopic;

import com.hengye.share.model.HotTopic;
import com.hengye.share.model.Topic;
import com.hengye.share.model.sina.WBCards;
import com.hengye.share.model.sina.WBHotTopic;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicReposts;
import com.hengye.share.module.util.encapsulation.base.TaskState;
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

public class HotTopicAndStatusPresenter extends ListDataPresenter<Topic, HotTopicAndStatusContract.View>
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
                            .flatMap(flatWBHotTopics()),
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
                            ArrayList<Topic> obj2 = (ArrayList<Topic>) objects[1];
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

    public Map<String, String> getWBAHotTopicParameter(int id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder();
        if (!isRefresh) {
            ub.addParameter("since_id", id);
        }
        return ub.getParameters();
    }


    Function<WBCards, SingleSource<ArrayList<Topic>>> mFlatWBTopics;

    /**
     * 把微博实体转换成自定义的实体
     * @return
     */
    public  Function<WBCards, SingleSource<ArrayList<Topic>>> flatWBTopics() {
        if (mFlatWBTopics == null) {
            mFlatWBTopics = new Function<WBCards, SingleSource<ArrayList<Topic>>>() {
                @Override
                public SingleSource<ArrayList<Topic>> apply(WBCards wbCards) {
                    return SingleHelper.justArrayList(Topic.getTopics(WBCards.listWBTopics(wbCards)));
                }
            };
        }
        return mFlatWBTopics;
    }


    Function<WBHotTopic, SingleSource<ArrayList<HotTopic>>> mFlatWBHotTopics;

    /**
     * 把微博实体转换成自定义的实体
     * @return
     */
    public  Function<WBHotTopic, SingleSource<ArrayList<HotTopic>>> flatWBHotTopics() {
        if (mFlatWBHotTopics == null) {
            mFlatWBHotTopics = new Function<WBHotTopic, SingleSource<ArrayList<HotTopic>>>() {
                @Override
                public SingleSource<ArrayList<HotTopic>> apply(WBHotTopic wbHotTopic) {
                    return SingleHelper.justArrayList(HotTopic.getHotTopics(wbHotTopic));
                }
            };
        }
        return mFlatWBHotTopics;
    }

}
