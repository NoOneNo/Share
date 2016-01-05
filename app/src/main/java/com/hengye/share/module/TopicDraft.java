package com.hengye.share.module;

import java.io.Serializable;

public class TopicDraft extends ParentInherit implements Serializable{

    private static final long serialVersionUID = -7695751557899945662L;

    private Topic topic;

    private int publishType;

    public final static int PUBLISHT_TOPIC = 0;
    public final static int PUBLISHT_COMMENT = 1;
    public final static int REPLY_COMMENT = 2;

    public TopicDraft(Topic topic, int publishType) {
        this.topic = topic;
        this.publishType = publishType;
    }

    public int getPublishType() {
        return publishType;
    }

    public void setPublishType(int publishType) {
        this.publishType = publishType;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicDraft that = (TopicDraft) o;

        if (publishType != that.publishType) return false;
        return !(topic != null ? !topic.equals(that.topic) : that.topic != null);

    }

    @Override
    public int hashCode() {
        int result = topic != null ? topic.hashCode() : 0;
        result = 31 * result + publishType;
        return result;
    }
}
