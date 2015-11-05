package com.hengye.share.module;

import com.hengye.share.module.sina.WBTopic;

import java.util.ArrayList;
import java.util.List;

public class Topic {

    public final static int TYPE_WB = 1;

    private String portrait;
    private String username;
    private String date;
    private String channel;
    private String content;

    private Object parent;
    private int parentType;

    public static List<Topic> getTopic(WBTopic wbTopic){
        List<Topic> topics = new ArrayList<>();
        for(WBTopic.StatusesEntity entity : wbTopic.getStatuses()){
            Topic topic = new Topic();
            topic.setParent(entity);
            topic.setParentType(TYPE_WB);
            topic.setPortrait(entity.getUser().getProfile_image_url());
            topic.setUsername(entity.getUser().getScreen_name());
            topic.setDate(entity.getCreated_at());
            topic.setChannel(entity.getSource());
            topic.setContent(entity.getText());
            topics.add(topic);
        }
        return topics;
    }



    @Override
    public String toString() {
        return "Topic{" +
                "portrait='" + portrait + '\'' +
                ", username='" + username + '\'' +
                ", date='" + date + '\'' +
                ", channel='" + channel + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public int getParentType() {
        return parentType;
    }

    public void setParentType(int parentType) {
        this.parentType = parentType;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
