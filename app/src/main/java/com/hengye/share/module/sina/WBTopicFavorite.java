package com.hengye.share.module.sina;

import java.util.List;

public class WBTopicFavorite {

    private WBTopic status;
    private List<WBTopicFavorites.WBTopicFavoritesTag> tags;
    private String favorited_time;

    public WBTopic getStatus() {
        return status;
    }

    public void setStatus(WBTopic status) {
        this.status = status;
    }

    public List<WBTopicFavorites.WBTopicFavoritesTag> getTags() {
        return tags;
    }

    public void setTags(List<WBTopicFavorites.WBTopicFavoritesTag> tags) {
        this.tags = tags;
    }

    public String getFavorited_time() {
        return favorited_time;
    }

    public void setFavorited_time(String favorited_time) {
        this.favorited_time = favorited_time;
    }
}
