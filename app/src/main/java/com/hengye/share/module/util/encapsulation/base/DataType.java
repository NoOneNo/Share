package com.hengye.share.module.util.encapsulation.base;

import android.view.View;

import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.List;

/**
 * Created by yuhy on 16/7/28.
 */
public class DataType {

    public static final int REFRESH_NO_MORE_DATA = 1;//刷新前有内容，刷新后没有新的内容
    public static final int REFRESH_NO_DATA = 2;//刷新前后都没有内容
    public static final int REFRESH_DATA_SIZE_LESS = 3;//刷新结果小于请求条数
    public static final int REFRESH_DATA_SIZE_EQUAL = 4;//刷新结果大于或等于请求条数
    public static final int LOAD_NO_DATA = 5;//没有数据可加载
    public static final int LOAD_NO_MORE_DATA = 6;//没有更多的数据可加载，即结果小于请求条数
    public static final int LOAD_DATA_SIZE_EQUAL = 7;//加载结果大于或等于请求条数

    public static int getType(boolean isRefresh, List data, List currentData) {
        int status;
        if (isRefresh) {
            //下拉刷新
            if (CommonUtil.isEmpty(data)) {
                //没有内容更新
                if (CommonUtil.isEmpty(currentData)) {
                    status = REFRESH_NO_DATA;
                } else {
                    status = REFRESH_NO_MORE_DATA;
                }
            } else if (data.size() < WBUtil.getWBStatusRequestCount() / 2) {
                //结果小于请求条数的一半， 不跟全部比较，因为微博开放接口返回数据不准确
                status = REFRESH_DATA_SIZE_LESS;
            } else {
                //结果大于或等于请求条数
                status = REFRESH_DATA_SIZE_EQUAL;
            }
        } else {
            //上拉加载
            if (CommonUtil.isEmpty(data)) {
                //没有数据可供加载
                status = LOAD_NO_DATA;
            } else {
                //成功加载更多
                if (data.size() < WBUtil.getWBStatusRequestCount()) {
                    //没有更多的数据可供加载
                    status = LOAD_NO_MORE_DATA;
                } else {
                    status = LOAD_DATA_SIZE_EQUAL;
                }
            }
        }
        return status;
    }

    public static boolean hasNewData(int type){
        return !(type == REFRESH_NO_MORE_DATA || type == REFRESH_NO_DATA || type == LOAD_NO_DATA);
    }

    public static void handleSnackBar(int type, View v, int size) {
        switch (type) {
            case REFRESH_NO_MORE_DATA:
                ToastUtil.showSnackBar(v, "没有新的微博");
                break;
            case REFRESH_NO_DATA:
                ToastUtil.showSnackBar(v, "暂时没有微博");
                break;
            case REFRESH_DATA_SIZE_LESS:
                ToastUtil.showSnackBar(v, size + "条新微博");
                //存储数据
                break;
            case REFRESH_DATA_SIZE_EQUAL:
                ToastUtil.showSnackBar(v, "超过" + WBUtil.getWBStatusRequestCount() + "条新微博");
                break;
            case LOAD_NO_MORE_DATA:
                break;
            case LOAD_NO_DATA:
                ToastUtil.showSnackBar(v, "已经是最后内容");
                break;
        }
    }
}
