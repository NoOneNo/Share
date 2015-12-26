package com.hengye.share.module;

public class TopicPublish extends ParentInherit{

    private Topic topic;

    private String token;

    public static TopicPublish getWBTopicPublish(Topic topic, String token){
        TopicPublish tp = new TopicPublish();
        tp.setTopic(topic);
        tp.setToken(token);
        tp.setParent(Parent.getWBParent());
        return tp;
    }

    @Override
    public String toString() {
        return "TopicPublish{" +
                "topic=" + topic +
                ", token='" + token + '\'' +
                '}';
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
