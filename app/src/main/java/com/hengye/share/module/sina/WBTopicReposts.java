package com.hengye.share.module.sina;

import java.io.Serializable;
import java.util.List;

public class WBTopicReposts implements Serializable{

    private static final long serialVersionUID = 4390846100266521520L;

    private List<WBTopic> reposts;

    private boolean hasvisible;
    private long previous_cursor;
    private long next_cursor;
    private long total_number;
    private int interval;

    @Override
    public String toString() {
        return "WBTopicReposts{" +
                "reposts=" + reposts +
                ", hasvisible=" + hasvisible +
                ", previous_cursor=" + previous_cursor +
                ", next_cursor=" + next_cursor +
                ", total_number=" + total_number +
                ", interval=" + interval +
                '}';
    }

    public List<WBTopic> getReposts() {
        return reposts;
    }

    public void setReposts(List<WBTopic> reposts) {
        this.reposts = reposts;
    }

    public boolean isHasvisible() {
        return hasvisible;
    }

    public void setHasvisible(boolean hasvisible) {
        this.hasvisible = hasvisible;
    }

    public long getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(long previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public long getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(long next_cursor) {
        this.next_cursor = next_cursor;
    }

    public long getTotal_number() {
        return total_number;
    }

    public void setTotal_number(long total_number) {
        this.total_number = total_number;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
