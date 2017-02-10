package com.hengye.share.model;

import com.hengye.share.model.sina.WBHotTopic;
import com.hengye.share.util.CommonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 2017/2/10.
 * 热门话题
 */

public class HotTopic implements Serializable{

    private static final long serialVersionUID = 8827277671542720593L;

    public static List<HotTopic> getHotTopics(WBHotTopic wbHotTopic) {
        if (wbHotTopic == null || CommonUtil.isEmpty(wbHotTopic.getTopics())) {
            return null;
        }

        List<HotTopic> hotTopics = new ArrayList<>();

        for (WBHotTopic.TopicsBean topicsBean : wbHotTopic.getTopics()) {
            hotTopics.add(new HotTopic(
                    topicsBean.getPic(),
                    topicsBean.getCard_type_name(),
                    topicsBean.getDesc1(),
                    topicsBean.getDesc2()
            ));
        }
        return hotTopics;
    }

    public HotTopic() {
    }

    public HotTopic(String cover, String topic, String description, String readingInfo) {
        this.cover = cover;
        this.topic = topic;
        this.description = description;
        this.readingInfo = readingInfo;
    }

    String cover;

    String topic;

    String description;

    String readingInfo;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReadingInfo() {
        return readingInfo;
    }

    public void setReadingInfo(String readingInfo) {
        this.readingInfo = readingInfo;
    }
}
