package com.hengye.share.model;

import java.io.Serializable;
import com.hengye.share.model.greenrobot.StatusDraft;

public class StatusPublish extends ParentInherit implements Serializable {

    private static final long serialVersionUID = 6509806716456702406L;

    private StatusDraft statusDraft;

    private String token;


    public StatusPublish() {
    }

    public StatusPublish(StatusDraft statusDraft, String token) {
        this.statusDraft = statusDraft;
        this.token = token;
    }

    public static StatusPublish getWBStatusPublish(StatusDraft statusDraft, String token) {
        StatusPublish tp = new StatusPublish();
        tp.setStatusDraft(statusDraft);
        tp.setToken(token);
        tp.setParent(Parent.getWBParent());
        return tp;
    }

    @Override
    public String toString() {
        return "TopicPublish{" +
                "topicDraft=" + statusDraft +
                ", token='" + token + '\'' +
                '}';
    }

    public StatusDraft getStatusDraft() {
        return statusDraft;
    }

    public void setStatusDraft(StatusDraft statusDraft) {
        this.statusDraft = statusDraft;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
