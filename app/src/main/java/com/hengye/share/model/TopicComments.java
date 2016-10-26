package com.hengye.share.model;

import java.util.List;

/**
 * Created by yuhy on 2016/10/26.
 */

public class TopicComments {

    private long totalNumber;

    private List<TopicComment> comments;

    public long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public List<TopicComment> getComments() {
        return comments;
    }

    public void setComments(List<TopicComment> comments) {
        this.comments = comments;
    }
}
