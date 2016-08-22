package com.hengye.share.handler.data;

import com.hengye.share.handler.data.base.Pager;

/**
 * Created by yuhy on 16/7/28.
 */
public class NumberPager extends Pager {

    public NumberPager(){
        this(1);
    }

    public NumberPager(int firstNumber){
        this.firstNumber = firstNumber;
        this.pageNumber = this.firstNumber;
    }

    private int firstNumber, pageNumber;

    @Override
    public Integer getFirstPage() {
        return firstNumber;
    }

    @Override
    public Integer getNextPage() {
        return pageNumber + 1;
    }

    @Override
    public void handlePage(boolean isRefresh) {
        if(isRefresh){
            pageNumber = firstNumber;
        }else{
            pageNumber++;
        }
    }

    @Override
    public Integer getPage(boolean isRefresh) {
        return (Integer) super.getPage(isRefresh);
    }

    public void setPageNumber(int pageNumber){
        this.pageNumber = pageNumber;
    }

    public Integer getPageNumber(){
        return this.pageNumber;
    }
}
