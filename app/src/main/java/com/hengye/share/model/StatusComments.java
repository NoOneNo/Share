package com.hengye.share.model;

import java.util.List;

/**
 * Created by yuhy on 2016/10/26.
 */

public class StatusComments {

    private static StatusComment LABEL_STATUS_HOT_COMMENT;

    public static StatusComment getStatusHotCommentLabel(){
        if(LABEL_STATUS_HOT_COMMENT == null){
            LABEL_STATUS_HOT_COMMENT = new StatusComment();
        }
        return LABEL_STATUS_HOT_COMMENT;
    }

    private long totalNumber;
    private long hotTotalNumber;

    private List<StatusComment> comments;

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

    public List<StatusComment> getComments() {
        return comments;
    }

    public void setComments(List<StatusComment> comments) {
        this.comments = comments;
    }

}
