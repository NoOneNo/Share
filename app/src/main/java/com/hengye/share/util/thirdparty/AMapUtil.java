package com.hengye.share.util.thirdparty;

import android.app.Activity;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hengye.share.R;
import com.hengye.share.module.base.ActivityHelper;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.test.TestLocationActivity;
import com.hengye.share.util.ApplicationUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;

/**
 * Created by yuhy on 2016/12/6.
 */

public class AMapUtil {

    public static AMapLocationClient initLocationClient(Activity activity, final AMapLocationListener aMapLocationListener){
        final AMapLocationClient locationClient = new AMapLocationClient(ApplicationUtil.getContext());
        //设置定位参数
        locationClient.setLocationOption(AMapUtil.getDefaultAMapLocationClientOption());
        // 设置定位监听
        locationClient.setLocationListener(aMapLocationListener);

        if(activity instanceof BaseActivity){
            ((BaseActivity)activity).getActivityHelper().registerActivityLifecycleListener(new ActivityHelper.DefaultActivityLifecycleListener(){
                @Override
                public void onActivityPaused(Activity activity) {
                    super.onActivityPaused(activity);
                    locationClient.stopLocation();
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    super.onActivityResumed(activity);
                    locationClient.startLocation();
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    super.onActivityDestroyed(activity);
                    locationClient.unRegisterLocationListener(aMapLocationListener);
                    locationClient.onDestroy();
                }
            });
        }

        return locationClient;
    }

    public static AMapLocationClientOption getDefaultAMapLocationClientOption(){
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        //高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(false);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        //设置是否等待设备wifi刷新，默认为false，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        //设置是否使用传感器
        locationOption.setSensorEnable(false);
        //设置是否开启wifi扫描，默认为true，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        locationOption.setWifiScan(true);

        // 设置网络请求超时时间，默认30秒
//        locationOption.setHttpTimeOut(30000);

        return locationOption;
    }
}
