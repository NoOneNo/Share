package com.hengye.share.model;

import java.io.Serializable;

public class TopicDraft2 extends ParentInherit implements Serializable{

    private static final long serialVersionUID = -7695751557899945662L;

    private Topic topic;

    private int publishType;

    private ExtraInfo extraInfo;

    public final static int PUBLISHT_TOPIC = 0;
    public final static int PUBLISHT_COMMENT = 1;
    public final static int REPLY_COMMENT = 2;
    public final static int REPOST_TOPIC = 3;

    public TopicDraft2() {}

    public TopicDraft2(Topic topic, int publishType) {
        this(topic, publishType, null);
    }

    public TopicDraft2(Topic topic, int publishType, ExtraInfo extraInfo) {
        this.topic = topic;
        this.publishType = publishType;
        this.extraInfo = extraInfo;
    }

    public static TopicDraft2 getTopicDraftByTopicRepost(String targetTopicId){
        TopicDraft2 topicDraft = new TopicDraft2();
        TopicDraft2.ExtraInfo extraInfo = new TopicDraft2.ExtraInfo();
        extraInfo.setTargetTopicId(targetTopicId);
        topicDraft.setExtraInfo(extraInfo);
        topicDraft.setPublishType(TopicDraft2.REPOST_TOPIC);
        return topicDraft;
    }

    public static TopicDraft2 getTopicDraftByTopicComment(String targetTopicId){
        TopicDraft2 topicDraft = new TopicDraft2();
        TopicDraft2.ExtraInfo extraInfo = new TopicDraft2.ExtraInfo();
        extraInfo.setTargetTopicId(targetTopicId);
        topicDraft.setExtraInfo(extraInfo);
        topicDraft.setPublishType(TopicDraft2.PUBLISHT_COMMENT);
        return topicDraft;
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

    public ExtraInfo getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(ExtraInfo extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopicDraft2 that = (TopicDraft2) o;

        if (publishType != that.publishType) return false;
        return !(topic != null ? !topic.equals(that.topic) : that.topic != null);

    }

    @Override
    public int hashCode() {
        int result = topic != null ? topic.hashCode() : 0;
        result = 31 * result + publishType;
        return result;
    }

    public static class ExtraInfo implements Serializable{

        private static final long serialVersionUID = -5033916278739175381L;

        String targetTopicId;
        String targetCommentId;
        int isCommentOrigin;//当评论转发微博时，是否评论给原微博，0：否、1：是，默认为0。
        int isComment;//是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0 。

        public String getTargetTopicId() {
            return targetTopicId;
        }

        public void setTargetTopicId(String targetTopicId) {
            this.targetTopicId = targetTopicId;
        }

        public String getTargetCommentId() {
            return targetCommentId;
        }

        public void setTargetCommentId(String targetCommentId) {
            this.targetCommentId = targetCommentId;
        }

        public int getIsCommentOrigin() {
            return isCommentOrigin;
        }

        public void setIsCommentOrigin(int isCommentOrigin) {
            this.isCommentOrigin = isCommentOrigin;
        }

        public int getIsComment() {
            return isComment;
        }

        public void setIsComment(int isComment) {
            this.isComment = isComment;
        }
    }
}
