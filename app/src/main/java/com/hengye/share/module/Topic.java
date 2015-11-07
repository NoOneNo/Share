package com.hengye.share.module;

import com.hengye.share.module.sina.WBTopic;

import java.util.ArrayList;

public class Topic extends Parent{

    private String avator;
    private String username;
    private String date;
    private String channel;
    private String content;

    public static ArrayList<Topic> getTopic(WBTopic wbTopic){
        ArrayList<Topic> topics = new ArrayList<>();
        for(WBTopic.StatusesEntity entity : wbTopic.getStatuses()){
            Topic topic = new Topic();
            topic.setParent(entity);
            topic.setParentType(Parent.TYPE_WEIBO);
            topic.setAvator(entity.getUser().getAvatar_large());
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
                "avator='" + avator + '\'' +
                ", username='" + username + '\'' +
                ", date='" + date + '\'' +
                ", channel='" + channel + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
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
