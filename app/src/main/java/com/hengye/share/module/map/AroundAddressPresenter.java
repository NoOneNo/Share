package com.hengye.share.module.map;

import com.hengye.share.model.Address;
import com.hengye.share.model.other.AMapAddresses;
import com.hengye.share.model.other.AMapModel;
import com.hengye.share.model.sina.WBAddresses;
import com.hengye.share.module.util.encapsulation.base.Pager;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.SingleHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

/**
 * Created by yuhy on 2016/12/6.
 */

public class AroundAddressPresenter extends ListDataPresenter<Address, AroundAddressContract.View>
        implements AroundAddressContract.Presenter{

    @Inject
    public AroundAddressPresenter(AroundAddressContract.View mvpView) {
        super(mvpView);
    }

    @Override
    public void loadAroundAddress(boolean isRefresh) {

        getMvpView().onTaskStart();

//        RetrofitManager
//                .getWBService()
//                .getPlaceNearBy(getWBAddressParams(isRefresh))
//                .flatMap(flatWBAddresses())
        RetrofitManager
                .getShareService()
                .getPlaceRoundByAMap(getAMapAddressParams(isRefresh))
                .flatMap(flatAMapAddresses(isRefresh))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new ListDataSingleObserver(isRefresh));
    }

    private Map<String, String> getAMapAddressParams(boolean isRefresh) {
        UrlBuilder urlBuilder = UrlBuilder.build()
                .addParameter("key", ThirdPartyUtils.getAppKeyForAMapWeb())
                .addParameter("location", longitude + "," + latitude)
                .addParameter("offset", 30)
                .addParameter("page", pager.getPage(isRefresh))
                .addParameter("extensions", "all");

        if (!CommonUtil.isEmpty(keywords)) {
            urlBuilder.addParameter("keywords", keywords);
        }
        return urlBuilder.getParameters();
    }

    private Map<String, String> getWBAddressParams(boolean isRefresh) {
        UrlBuilder urlBuilder = UrlBuilder.build()
                .addParameter("access_token", UserUtil.getToken())
                .addParameter("long", longitude)
                .addParameter("lat", latitude)
                .addParameter("count", 30)
                .addParameter("page", pager.getPage(isRefresh))
                .addParameter("sort", 1)//按距离排序
                .addParameter("offset", 1);//传入的经纬度是否是纠偏过，0：没纠偏、1：纠偏过，默认为0。

        if (!CommonUtil.isEmpty(keywords)) {
            urlBuilder.addParameter("q", keywords);
        }
        return urlBuilder.getParameters();
    }


    private Function<AMapAddresses, SingleSource<ArrayList<Address>>> flatAMapAddresses(final boolean isRefresh) {
        return new Function<AMapAddresses, SingleSource<ArrayList<Address>>>() {
            @Override
            public SingleSource<ArrayList<Address>> apply(AMapAddresses aMapAddresses) throws Exception {
                if (aMapAddresses != null && AMapModel.isSucess(aMapAddresses.getStatus())) {
                    return SingleHelper.justArrayList(aMapAddresses.convert());
                } else {
                    return RetrofitManager
                            .getWBService()
                            .getPlaceNearBy(getWBAddressParams(isRefresh))
                            .flatMap(flatWBAddresses());
                }
            }
        };
    }

    Function<WBAddresses, SingleSource<ArrayList<Address>>> mFlatWBAddresses;

    private Function<WBAddresses, SingleSource<ArrayList<Address>>> flatWBAddresses() {
        if (mFlatWBAddresses == null) {
            mFlatWBAddresses = new Function<WBAddresses, SingleSource<ArrayList<Address>>>() {
                @Override
                public SingleSource<ArrayList<Address>> apply(WBAddresses wbAddresses) throws Exception {
                    if (wbAddresses != null) {
                        return SingleHelper.justArrayList(wbAddresses.convert());
                    } else {
                        return SingleHelper.justArrayList(null);
                    }
                }
            };
        }
        return mFlatWBAddresses;
    }

    private String keywords;

    /**
     * 经纬度
     */
    private float longitude, latitude;
    private Pager pager;

    @Override
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public void setLocation(float longitude, float latitude) {
        setLongitude(longitude);
        setLatitude(latitude);
    }

    @Override
    public void setPager(Pager pager) {
        this.pager = pager;
    }

    private void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    private void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
