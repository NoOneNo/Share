package com.hengye.share.module.sina;

import java.io.Serializable;
import java.util.List;

public class WBTopics implements Serializable{

    private static final long serialVersionUID = -6946098874302619957L;

    /**
     * https://api.weibo.com/2/statuses/friends_timeline.json
     */
//    必选 	类型及范围 	说明
//    source 	false 	string 	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
//    access_token 	false 	string 	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
//    since_id 	false 	int64 	若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
//    max_id 	false 	int64 	若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
//    count 	false 	int 	单页返回的记录条数，最大不超过100，默认为20。
//    page 	false 	int 	返回结果的页码，默认为1。
//    base_app 	false 	int 	是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
//    feature 	false 	int 	过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
//    trim_user 	false 	int 	返回值中user字段开关，0：返回完整user字段、1：user字段仅返回user_id，默认为0。

    /**
     * advertises : []
     * ad : []
     * hasvisible : false
     * previous_cursor : 0
     * next_cursor : 3904841749174397
     * total_number : 150
     * interval : 2000
     * uve_blank : -1
     * since_id : 3904859252596728
     * max_id : 3904841749174397
     * has_unread : 0
     */

    private boolean hasvisible;
    private long previous_cursor;
    private long next_cursor;
    private int total_number;
    private int interval;
    private int uve_blank;
    private long since_id;
    private long max_id;
    private int has_unread;

    private List<WBTopic> statuses;
    private List<?> advertises;
    private List<?> ad;

    public void setHasvisible(boolean hasvisible) {
        this.hasvisible = hasvisible;
    }

    public void setPrevious_cursor(long previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public void setNext_cursor(long next_cursor) {
        this.next_cursor = next_cursor;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setUve_blank(int uve_blank) {
        this.uve_blank = uve_blank;
    }

    public void setSince_id(long since_id) {
        this.since_id = since_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }

    public void setHas_unread(int has_unread) {
        this.has_unread = has_unread;
    }

    public void setStatuses(List<WBTopic> statuses) {
        this.statuses = statuses;
    }

    public void setAdvertises(List<?> advertises) {
        this.advertises = advertises;
    }

    public void setAd(List<?> ad) {
        this.ad = ad;
    }

    public boolean isHasvisible() {
        return hasvisible;
    }

    public long getPrevious_cursor() {
        return previous_cursor;
    }

    public long getNext_cursor() {
        return next_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public int getInterval() {
        return interval;
    }

    public int getUve_blank() {
        return uve_blank;
    }

    public long getSince_id() {
        return since_id;
    }

    public long getMax_id() {
        return max_id;
    }

    public int getHas_unread() {
        return has_unread;
    }

    public List<WBTopic> getStatuses() {
        return statuses;
    }

    public List<?> getAdvertises() {
        return advertises;
    }

    public List<?> getAd() {
        return ad;
    }

}
