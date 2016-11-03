package com.hengye.share.module.util.encapsulation.base;

/**
 * Created by yuhy on 16/7/28.
 */
public class NumberPager extends Pager {

    public NumberPager(){
        this(1);
    }

    public NumberPager(int firstNumber){
        this(firstNumber, 16);
    }

    public NumberPager(int firstNumber, int pageSize){
        this.firstNumber = firstNumber;
        this.pageNumber = firstNumber;
        this.pageSize = pageSize;
    }

    private int firstNumber, pageNumber, pageSize;

    @Override
    public Integer getFirstPage() {
        return firstNumber;
    }

    @Override
    public Integer getNextPage() {
        return pageNumber + 1;
    }

    @Override
    public Integer getPage(boolean isRefresh) {
        return (Integer)super.getPage(isRefresh);
    }

    @Override
    public void handlePage(boolean isRefresh) {
        if(isRefresh){
            pageNumber = firstNumber;
        }else{
            pageNumber++;
        }
    }

    public void setPageNumber(int pageNumber){
        this.pageNumber = pageNumber;
    }

    public Integer getPageNumber(){
        return this.pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }
}
