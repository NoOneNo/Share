package com.hengye.share.model.sina;

import java.util.List;

public class WBStatusIds {
    /**
     * statuses : ["3908848957992365","3908848685329179","3908848400689341","3908848341178987","3908846152386534","3908846130954511","3908844180526052","3908843861980056","3908841978643345","3908841932124599","3908841873689899","3908841114240333","3908840791979044","3908839965500224","3908839839507619","3908839080348709","3908838942187085","3908838581148386","3908838581148244","3908838450990159"]
     * advertises : []
     * ad : []
     * hasvisible : false
     * previous_cursor : 0
     * next_cursor : 3908837943530156
     * total_number : 150
     * interval : 0
     * uve_blank : -1
     */

    private boolean hasvisible;
    private long previous_cursor;
    private long next_cursor;
    private long total_number;
    private int interval;
    private int uve_blank;
    private List<String> statuses;
    private List<?> advertises;
    private List<?> ad;

    @Override
    public String toString() {
        return "WBStatusIds{" +
                "hasvisible=" + hasvisible +
                ", previous_cursor=" + previous_cursor +
                ", next_cursor=" + next_cursor +
                ", total_number=" + total_number +
                ", interval=" + interval +
                ", uve_blank=" + uve_blank +
                ", statuses=" + statuses +
                ", advertises=" + advertises +
                ", ad=" + ad +
                '}';
    }

    public void setHasvisible(boolean hasvisible) {
        this.hasvisible = hasvisible;
    }

    public void setPrevious_cursor(long previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public void setNext_cursor(long next_cursor) {
        this.next_cursor = next_cursor;
    }

    public void setTotal_number(long total_number) {
        this.total_number = total_number;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setUve_blank(int uve_blank) {
        this.uve_blank = uve_blank;
    }

    public void setStatuses(List<String> statuses) {
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

    public long getTotal_number() {
        return total_number;
    }

    public int getInterval() {
        return interval;
    }

    public int getUve_blank() {
        return uve_blank;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public List<?> getAdvertises() {
        return advertises;
    }

    public List<?> getAd() {
        return ad;
    }
}
