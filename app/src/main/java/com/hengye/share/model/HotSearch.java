package com.hengye.share.model;

import com.hengye.share.model.sina.WBHotSearch;
import com.hengye.share.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 2017/2/11.
 */

public class HotSearch {

    public static List<HotSearch> listHotSearch(WBHotSearch wbHotSearch){
        if(wbHotSearch == null || CommonUtil.isEmpty(wbHotSearch.getData())){
            return null;
        }

        List<HotSearch> hotSearches = new ArrayList<>();

        for(WBHotSearch.DataBean dataBean : wbHotSearch.getData()){
            hotSearches.add(new HotSearch(dataBean.getTitle(), CommonUtil.getLongValue(dataBean.getNumber())));
        }
        return hotSearches;
    }

    public HotSearch(){}

    public HotSearch(String content, long count) {
        this.content = content;
        this.count = count;
    }

    String content;
    long count;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

