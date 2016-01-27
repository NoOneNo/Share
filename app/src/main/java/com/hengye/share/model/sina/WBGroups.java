package com.hengye.share.model.sina;

import java.util.List;

public class WBGroups {

    /**
     * total_number : 11
     */

    private int total_number;
    /**
     * id : 201108150241527780
     * idstr : 201108150241527776
     * name : 特别关注
     * mode : system
     * visible : -1
     * like_count : 0
     * member_count : 13
     * description :
     * tags : []
     * profile_image_url : http://tp4.sinaimg.cn/1764328631/50/40096799156/1
     * created_at : Mon Aug 15 15:01:04 +0800 2011
     */

    private List<WBGroup> lists;

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public void setLists(List<WBGroup> lists) {
        this.lists = lists;
    }

    public int getTotal_number() {
        return total_number;
    }

    public List<WBGroup> getLists() {
        return lists;
    }

    @Override
    public String toString() {
        return "WBGroups{" +
                "total_number=" + total_number +
                ", lists=" + lists +
                '}';
    }

    public static class WBGroupUpdateOrder{

        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }
}
