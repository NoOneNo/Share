package com.hengye.share.module;

import android.support.v7.util.AsyncListUtil;
import android.text.TextUtils;

import com.hengye.share.module.sina.WBTopic;
import com.hengye.share.util.CommonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Topic extends Parent{

    private String avator;
    private String username;
    private String date;
    private String channel;
    private String content;
    private List<String> imageUrls;

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
            if(!CommonUtil.isEmptyList(entity.getPic_urls())){
                List<String> urls = new ArrayList<>();
                for(WBTopic.StatusesEntity.Pic_urlsEntity urlsEntity : entity.getPic_urls()){
                    urls.add(getWBTopicImgUrl(urlsEntity.getThumbnail_pic(), IMAGE_TYPE_BMIDDLE));
                }
                topic.setImageUrls(urls);
            }
            topics.add(topic);
        }
        return topics;
    }

    public static int IMAGE_TYPE_BMIDDLE = 1;//高清
    public static int IMAGE_TYPE_LARGE = 2;//原图
    //默认返回缩略图
    //要得到高清图或者原图，把地址"http://ww1.sinaimg.cn/thumbnail/6dab804cjw1exv392snomj21kw23ukjl.jpg"
    //中的thumbnail换成对应的bmiddle(高清)或者large(原图)
    public static String getWBTopicImgUrl(String url, int type){
        if(TextUtils.isEmpty(url)){
            return null;
        }
        if(type == IMAGE_TYPE_BMIDDLE){
            return url.replaceFirst("thumbnail", "bmiddle");
        }else if(type == IMAGE_TYPE_LARGE){
            return url.replaceFirst("thumbnail", "large");
        }else {
            return url;
        }
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
