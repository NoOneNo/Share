package com.hengye.share.util;

import android.view.View;

import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.module.Topic;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;

import java.util.List;

public class DataUtil {


    public final static int REFRESH_NO_MORE_DATA = 1;//刷新前有内容，刷新后没有新的内容
    public final static int REFRESH_NO_DATA = 2;//刷新前后都没有内容
    public final static int REFRESH_DATA_SIZE_LESS = 3;//刷新结果小于请求条数
    public final static int REFRESH_DATA_SIZE_EQUAL = 4;//刷新结果大于或等于请求条数
    public final static int LOAD_NO_DATA = 5;//没有数据可加载
    public final static int LOAD_NO_MORE_DATA = 6;//没有更多的数据可加载，即结果小于请求条数
    public final static int LOAD_DATA_SIZE_EQUAL = 7;//加载结果大于或等于请求条数

    public static <T> int handlePagingData(List<T> adapterData, List<T> data, boolean isRefresh) {

        if (isRefresh) {
            //下拉刷新
            if (CommonUtil.isEmptyCollection(data)) {
                //没有内容更新
                if (CommonUtil.isEmptyCollection(adapterData)) {
                    return REFRESH_NO_DATA;
                } else {
                    return REFRESH_NO_MORE_DATA;
                }
            } else if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                //结果小于请求条数
                return REFRESH_DATA_SIZE_LESS;
            } else {
                //结果大于或等于请求条数
                return REFRESH_DATA_SIZE_EQUAL;
            }
        } else {
            //上拉加载
            if (CommonUtil.isEmptyCollection(data)) {
                //没有数据可供加载
                return LOAD_NO_DATA;
            } else {
                //成功加载更多
                if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                    //没有更多的数据可供加载
                    return LOAD_NO_MORE_DATA;
                }else{
                    return LOAD_DATA_SIZE_EQUAL;
                }
            }
        }

    }

    public static <T, VH extends CommonAdapter.ItemViewHolder>
    void handleCommonAdapter(int type, CommonAdapter<T, VH> adapter, List<T> data){
        switch (type){
            case REFRESH_DATA_SIZE_LESS:
                adapter.addAll(0, data);
                break;
            case REFRESH_DATA_SIZE_EQUAL:
                adapter.refresh(data);
                break;
            case LOAD_NO_MORE_DATA:
            case LOAD_DATA_SIZE_EQUAL:
                adapter.addAll(data);
                break;
        }
    }

    public static <VH extends CommonAdapter.ItemViewHolder>
    void handleTopicAdapter(int type, CommonAdapter<Topic, VH> adapter, List<Topic> data){
        switch (type){
            case REFRESH_DATA_SIZE_LESS:
                adapter.addAll(0, data);
                break;
            case REFRESH_DATA_SIZE_EQUAL:
                adapter.refresh(data);
                break;
            case LOAD_NO_MORE_DATA:
            case LOAD_DATA_SIZE_EQUAL:
                //因为请求的数据是小于或等于max_id，需要做是否重复判断处理
                if (data.get(0).getId() != null && data.get(0).getId().
                        equals(CommonUtil.getLastItem(adapter.getData()).getId())) {
                    data.remove(0);
                }
                adapter.addAll(data);
                break;
        }
    }

    public static void handleSnackBar(int type, View v, int size){
        switch (type) {
            case REFRESH_NO_MORE_DATA:
                ToastUtil.showSnackBar("没有新的微博", v);
                break;
            case REFRESH_NO_DATA:
                ToastUtil.showSnackBar("暂时没有微博", v);
                break;
            case REFRESH_DATA_SIZE_LESS:
                ToastUtil.showSnackBar(size + "条新微博", v);
                //存储数据
                break;
            case REFRESH_DATA_SIZE_EQUAL:
                ToastUtil.showSnackBar("超过" + WBUtil.MAX_COUNT_REQUEST + "条新微博", v);
                break;
            case LOAD_NO_DATA:
            case LOAD_NO_MORE_DATA:
                ToastUtil.showSnackBar("已经是最后内容", v);
                break;
        }
    }

    public static void handlePullToRefresh(int type, PullToRefreshLayout pullToRefreshLayout) {
        switch (type) {
            case REFRESH_DATA_SIZE_EQUAL:
                pullToRefreshLayout.setLoadEnable(true);
                break;
            case LOAD_NO_DATA:
                pullToRefreshLayout.setLoadEnable(false);
                break;
            case LOAD_NO_MORE_DATA:
                pullToRefreshLayout.setLoadEnable(false);
                break;
        }
    }
}




























