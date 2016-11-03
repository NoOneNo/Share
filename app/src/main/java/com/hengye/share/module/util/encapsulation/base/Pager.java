package com.hengye.share.module.util.encapsulation.base;

/**
 * Created by yuhy on 16/7/27.
 * 分页器
 */
public abstract class Pager {

    abstract public Object getFirstPage();

    abstract public Object getNextPage();

    abstract public void handlePage(boolean isRefresh);

    abstract public int getPageSize();

    public Object getPage(boolean isRefresh){
        if(isRefresh){
            return getFirstPage();
        }else{
            return getNextPage();
        }
    }

}
