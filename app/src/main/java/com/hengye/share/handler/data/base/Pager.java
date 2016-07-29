package com.hengye.share.handler.data.base;

/**
 * Created by yuhy on 16/7/27.
 * 分页器
 */
public abstract class Pager {

//    abstract public DataAdapter<T> getPagingAdapter();

    abstract public Object getFirstPage();

    abstract public Object getNextPage();

    abstract public void handlePage(boolean isRefresh);

    public Object getPage(boolean isRefresh){
        if(isRefresh){
            return getFirstPage();
        }else{
            return getNextPage();
        }
    }


//    abstract public void handleData(boolean isFirstPage, List<T> data);
//
//    abstract public boolean isCanLoadMore(boolean isFirstPage, List<T> data);
//    /**
//     * @param data
//     * @return 是否可以加载更多
//     */
//    public boolean handleData(boolean isFirstPage, List data){
//        if(data == null || data.isEmpty()){
//            return false;
//        }
//        return true;
//    }

}
