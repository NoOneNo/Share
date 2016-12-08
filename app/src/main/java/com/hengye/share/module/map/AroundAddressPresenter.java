package com.hengye.share.module.map;

import com.hengye.share.model.Address;
import com.hengye.share.model.other.AMapAddresses;
import com.hengye.share.model.other.AMapModel;
import com.hengye.share.model.sina.WBAddresses;
import com.hengye.share.module.util.encapsulation.mvp.ListDataPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by yuhy on 2016/12/6.
 */

public class AroundAddressPresenter extends ListDataPresenter<Address, AroundAddressMvpView> {

    public AroundAddressPresenter(AroundAddressMvpView mvpView) {
        super(mvpView);
    }

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
                .subscribe(new ListDataSubscriber(isRefresh));
    }

    public Map<String, String> getAMapAddressParams(boolean isRefresh) {
        UrlBuilder urlBuilder = UrlBuilder.build()
                .addParameter("key", ThirdPartyUtils.getAppKeyForAMapWeb())
                .addParameter("location", getLongitude() + "," + getLatitude())
                .addParameter("offset", 30)
                .addParameter("page", getPager().getPage(isRefresh))
                .addParameter("extensions", "all");

        if (!CommonUtil.isEmpty(getKeywords())) {
            urlBuilder.addParameter("keywords", getKeywords());
        }
        return urlBuilder.getParameters();
    }

    public Map<String, String> getWBAddressParams(boolean isRefresh) {
        UrlBuilder urlBuilder = UrlBuilder.build()
                .addParameter("access_token", UserUtil.getToken())
                .addParameter("long", getLongitude())
                .addParameter("lat", getLatitude())
                .addParameter("count", 30)
                .addParameter("page", getPager().getPage(isRefresh))
                .addParameter("sort", 1)//按距离排序
                .addParameter("offset", 1);//传入的经纬度是否是纠偏过，0：没纠偏、1：纠偏过，默认为0。

        if (!CommonUtil.isEmpty(getKeywords())) {
            urlBuilder.addParameter("q", getKeywords());
        }
        return urlBuilder.getParameters();
    }


    private Function<AMapAddresses, ObservableSource<ArrayList<Address>>> flatAMapAddresses(final boolean isRefresh) {
        return new Function<AMapAddresses, ObservableSource<ArrayList<Address>>>() {
            @Override
            public ObservableSource<ArrayList<Address>> apply(AMapAddresses aMapAddresses) throws Exception {
                if (aMapAddresses != null && AMapModel.isSucess(aMapAddresses.getStatus())) {
                    return ObservableHelper.justArrayList(aMapAddresses.convert());
                } else {
                    return RetrofitManager
                            .getWBService()
                            .getPlaceNearBy(getWBAddressParams(isRefresh))
                            .flatMap(flatWBAddresses());
                }
            }
        };
    }

    Function<WBAddresses, ObservableSource<ArrayList<Address>>> mFlatWBAddresses;

    private Function<WBAddresses, ObservableSource<ArrayList<Address>>> flatWBAddresses() {
        if (mFlatWBAddresses == null) {
            mFlatWBAddresses = new Function<WBAddresses, ObservableSource<ArrayList<Address>>>() {
                @Override
                public ObservableSource<ArrayList<Address>> apply(WBAddresses wbAddresses) throws Exception {
                    if (wbAddresses != null) {
                        return ObservableHelper.justArrayList(wbAddresses.convert());
                    } else {
                        return ObservableHelper.just(null);
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public float[] getLocation() {
        return new float[]{longitude, latitude};
    }

    public void setLocation(float longitude, float latitude) {
        setLongitude(longitude);
        setLatitude(latitude);
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
