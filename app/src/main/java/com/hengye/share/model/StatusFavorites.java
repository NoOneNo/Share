package com.hengye.share.model;

import com.hengye.share.model.sina.WBStatusFavorite;
import com.hengye.share.model.sina.WBStatusFavorites;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatusFavorites {

    public static ArrayList<Status> getStatues(WBStatusFavorites wbStatusFavorites){
        if(wbStatusFavorites == null || CommonUtil.isEmpty(wbStatusFavorites.getFavorites())){
            return null;
        }
        ArrayList<Status> statuses = new ArrayList<>();
        for(WBStatusFavorite entity : wbStatusFavorites.getFavorites()){
            statuses.add(getStatusFavorite(entity).getStatus());
        }
        return statuses;
    }

    public static ArrayList<StatusFavorite> getStatusFavorites(WBStatusFavorites wbStatusFavorites){
        if(wbStatusFavorites == null || CommonUtil.isEmpty(wbStatusFavorites.getFavorites())){
            return null;
        }
        ArrayList<StatusFavorite> statusFavorites = new ArrayList<>();
        for(WBStatusFavorite entity : wbStatusFavorites.getFavorites()){
            statusFavorites.add(getStatusFavorite(entity));
        }
        return statusFavorites;
    }

    public static StatusFavorite getStatusFavorite(WBStatusFavorite entity){
        StatusFavorite statusFavorite = new StatusFavorite();
        statusFavorite.setParent(new Parent(GsonUtil.toJson(entity), Parent.TYPE_WEIBO));
        if(entity == null){
            return statusFavorite;
        }
        statusFavorite.setStatus(Status.getStatus(entity.getStatus()));
        statusFavorite.setTags(StatusFavoritesTag.getStatusFavoritesTags(entity.getTags()));

        return statusFavorite;
    }



    private List<StatusFavorite> favorites;

    private long total_number;

    @Override
    public String toString() {
        return "TopicFavorites{" +
                "favorites=" + favorites +
                ", total_number=" + total_number +
                '}';
    }

    public List<StatusFavorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<StatusFavorite> favorites) {
        this.favorites = favorites;
    }

    public long getTotal_number() {
        return total_number;
    }

    public void setTotal_number(long total_number) {
        this.total_number = total_number;
    }

    public static class StatusFavorite extends ParentInherit implements Serializable{

        private static final long serialVersionUID = 3996863356961458560L;

        private Status status;
        private List<StatusFavoritesTag> tags;
        private String favorited_time;

        @Override
        public String toString() {
            return "StatusFavorite{" +
                    "status=" + status +
                    ", tags=" + tags +
                    ", favorited_time='" + favorited_time + '\'' +
                    '}';
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public List<StatusFavoritesTag> getTags() {
            return tags;
        }

        public void setTags(List<StatusFavoritesTag> tags) {
            this.tags = tags;
        }

        public String getFavorited_time() {
            return favorited_time;
        }

        public void setFavorited_time(String favorited_time) {
            this.favorited_time = favorited_time;
        }
    }


    public static class StatusFavoritesTag implements Serializable{

        private static final long serialVersionUID = 2143638244203943490L;

        private long id;
        private String tag;
        private long count;

        public static ArrayList<StatusFavoritesTag> getStatusFavoritesTags(List<WBStatusFavorites.WBStatusFavoritesTag> wbStatusFavoritesTags){
            if(CommonUtil.isEmpty(wbStatusFavoritesTags)){
                return null;
            }
            ArrayList<StatusFavoritesTag> statusFavoritesTags = new ArrayList<>();
            for(WBStatusFavorites.WBStatusFavoritesTag entity : wbStatusFavoritesTags){
                statusFavoritesTags.add(getStatusFavoritesTag(entity));
            }
            return statusFavoritesTags;
        }

        public static StatusFavoritesTag getStatusFavoritesTag(WBStatusFavorites.WBStatusFavoritesTag wbStatusFavoritesTag){
            StatusFavoritesTag statusFavoritesTag = new StatusFavoritesTag();
            if(wbStatusFavoritesTag == null){
                return statusFavoritesTag;
            }
            statusFavoritesTag.setCount(wbStatusFavoritesTag.getCount());
            statusFavoritesTag.setId(wbStatusFavoritesTag.getId());
            statusFavoritesTag.setTag(wbStatusFavoritesTag.getTag());

            return statusFavoritesTag;
        }

        @Override
        public String toString() {
            return "StatusFavoritesTag{" +
                    "id=" + id +
                    ", tag='" + tag + '\'' +
                    ", count=" + count +
                    '}';
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }
}
