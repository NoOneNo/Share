package com.hengye.share.model;

import java.io.Serializable;
import com.hengye.share.model.greenrobot.TopicDraft;

public class TopicPublish extends ParentInherit implements Serializable {

    private static final long serialVersionUID = 6509806716456702406L;

    private TopicDraft topicDraft;

    private String token;


    public TopicPublish() {
    }

    public TopicPublish(TopicDraft topicDraft, String token) {
        this.topicDraft = topicDraft;
        this.token = token;
    }

    public static TopicPublish getWBTopicPublish(TopicDraft topicDraft, String token) {
        TopicPublish tp = new TopicPublish();
        tp.setTopicDraft(topicDraft);
        tp.setToken(token);
        tp.setParent(Parent.getWBParent());
        return tp;
    }

    @Override
    public String toString() {
        return "TopicPublish{" +
                "topicDraft=" + topicDraft +
                ", token='" + token + '\'' +
                '}';
    }

    public TopicDraft getTopicDraft() {
        return topicDraft;
    }

    public void setTopicDraft(TopicDraft topicDraft) {
        this.topicDraft = topicDraft;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
