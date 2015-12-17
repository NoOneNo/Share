package com.hengye.share.module.sina;

import java.util.List;

public class WBTopicFavorites {

    private List<WBTopicFavorite> favorites;

    private long total_number;

    public List<WBTopicFavorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<WBTopicFavorite> favorites) {
        this.favorites = favorites;
    }

    public long getTotal_number() {
        return total_number;
    }

    public void setTotal_number(long total_number) {
        this.total_number = total_number;
    }


    public static class WBTopicFavoritesTag {
        private long id;
        private String tag;
        private long count;

        @Override
        public String toString() {
            return "WBTopicFavoritesTag{" +
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
