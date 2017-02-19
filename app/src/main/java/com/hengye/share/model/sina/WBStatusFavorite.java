package com.hengye.share.model.sina;

import java.util.List;

public class WBStatusFavorite {

    private WBStatus status;
    private List<WBStatusFavorites.WBStatusFavoritesTag> tags;
    private String favorited_time;

    public WBStatus getStatus() {
        return status;
    }

    public void setStatus(WBStatus status) {
        this.status = status;
    }

    public List<WBStatusFavorites.WBStatusFavoritesTag> getTags() {
        return tags;
    }

    public void setTags(List<WBStatusFavorites.WBStatusFavoritesTag> tags) {
        this.tags = tags;
    }

    public String getFavorited_time() {
        return favorited_time;
    }

    public void setFavorited_time(String favorited_time) {
        this.favorited_time = favorited_time;
    }
}
