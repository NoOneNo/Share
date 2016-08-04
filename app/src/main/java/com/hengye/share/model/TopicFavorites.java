package com.hengye.share.model;

import com.hengye.share.model.sina.WBTopicFavorite;
import com.hengye.share.model.sina.WBTopicFavorites;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopicFavorites {

    public static ArrayList<TopicFavorite> getTopicFavorites(WBTopicFavorites wbTopicFavorites){
        if(wbTopicFavorites == null || CommonUtil.isEmpty(wbTopicFavorites.getFavorites())){
            return null;
        }
        ArrayList<TopicFavorite> topicFavorites = new ArrayList<>();
        for(WBTopicFavorite entity : wbTopicFavorites.getFavorites()){
            topicFavorites.add(getTopicFavorite(entity));
        }
        return topicFavorites;
    }

    public static TopicFavorite getTopicFavorite(WBTopicFavorite entity){
        TopicFavorite topicFavorite = new TopicFavorite();
        topicFavorite.setParent(new Parent(GsonUtil.toJson(entity), Parent.TYPE_WEIBO));
        if(entity == null){
            return topicFavorite;
        }
        topicFavorite.setTopic(Topic.getTopic(entity.getStatus()));
        topicFavorite.setTags(TopicFavoritesTag.getTopicFavoritesTags(entity.getTags()));

        return topicFavorite;
    }



    private List<TopicFavorite> favorites;

    private long total_number;

    @Override
    public String toString() {
        return "TopicFavorites{" +
                "favorites=" + favorites +
                ", total_number=" + total_number +
                '}';
    }

    public List<TopicFavorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<TopicFavorite> favorites) {
        this.favorites = favorites;
    }

    public long getTotal_number() {
        return total_number;
    }

    public void setTotal_number(long total_number) {
        this.total_number = total_number;
    }

    public static class TopicFavorite extends ParentInherit implements Serializable{

        private static final long serialVersionUID = 3996863356961458560L;

        private Topic topic;
        private List<TopicFavoritesTag> tags;
        private String favorited_time;

        @Override
        public String toString() {
            return "TopicFavorite{" +
                    "topic=" + topic +
                    ", tags=" + tags +
                    ", favorited_time='" + favorited_time + '\'' +
                    '}';
        }

        public Topic getTopic() {
            return topic;
        }

        public void setTopic(Topic topic) {
            this.topic = topic;
        }

        public List<TopicFavoritesTag> getTags() {
            return tags;
        }

        public void setTags(List<TopicFavoritesTag> tags) {
            this.tags = tags;
        }

        public String getFavorited_time() {
            return favorited_time;
        }

        public void setFavorited_time(String favorited_time) {
            this.favorited_time = favorited_time;
        }
    }


    public static class TopicFavoritesTag implements Serializable{

        private static final long serialVersionUID = 2143638244203943490L;

        private long id;
        private String tag;
        private long count;

        public static ArrayList<TopicFavoritesTag> getTopicFavoritesTags(List<WBTopicFavorites.WBTopicFavoritesTag> wbTopicFavoritesTags){
            if(CommonUtil.isEmpty(wbTopicFavoritesTags)){
                return null;
            }
            ArrayList<TopicFavoritesTag> topicFavoritesTags = new ArrayList<>();
            for(WBTopicFavorites.WBTopicFavoritesTag entity : wbTopicFavoritesTags){
                topicFavoritesTags.add(getTopicFavoritesTag(entity));
            }
            return topicFavoritesTags;
        }

        public static TopicFavoritesTag getTopicFavoritesTag(WBTopicFavorites.WBTopicFavoritesTag wbTopicFavoritesTag){
            TopicFavoritesTag topicFavoritesTag = new TopicFavoritesTag();
            if(wbTopicFavoritesTag == null){
                return topicFavoritesTag;
            }
            topicFavoritesTag.setCount(wbTopicFavoritesTag.getCount());
            topicFavoritesTag.setId(wbTopicFavoritesTag.getId());
            topicFavoritesTag.setTag(wbTopicFavoritesTag.getTag());

            return topicFavoritesTag;
        }

        @Override
        public String toString() {
            return "TopicFavoritesTag{" +
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
