package com.hengye.share.model;

import java.util.List;

/**
 * Created by yuhy on 2016/10/26.
 */

public class TopicComments {

    private static TopicComment LABEL_TOPIC_HOT_COMMENT;

    public static TopicComment getTopicHotCommentLabel(){
        if(LABEL_TOPIC_HOT_COMMENT == null){
            LABEL_TOPIC_HOT_COMMENT = new TopicComment();
        }
        return LABEL_TOPIC_HOT_COMMENT;
    }

    private long totalNumber;
    private long hotTotalNumber;

    private List<TopicComment> comments;

    public long getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(long totalNumber) {
        this.totalNumber = totalNumber;
    }

    public long getHotTotalNumber() {
        return hotTotalNumber;
    }

    public void setHotTotalNumber(long hotTotalNumber) {
        this.hotTotalNumber = hotTotalNumber;
    }

    public List<TopicComment> getComments() {
        return comments;
    }

    public void setComments(List<TopicComment> comments) {
        this.comments = comments;
    }

}
