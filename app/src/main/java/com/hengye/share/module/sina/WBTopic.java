package com.hengye.share.module.sina;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WBTopic implements Serializable{

    private static final long serialVersionUID = -6946098874302619957L;

    /**
     * https://api.weibo.com/2/statuses/friends_timeline.json
     */
//    必选 	类型及范围 	说明
//    source 	false 	string 	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
//    access_token 	false 	string 	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
//    since_id 	false 	int64 	若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
//    max_id 	false 	int64 	若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
//    count 	false 	int 	单页返回的记录条数，最大不超过100，默认为20。
//    page 	false 	int 	返回结果的页码，默认为1。
//    base_app 	false 	int 	是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
//    feature 	false 	int 	过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
//    trim_user 	false 	int 	返回值中user字段开关，0：返回完整user字段、1：user字段仅返回user_id，默认为0。


    /**
     * advertises : []
     * ad : []
     * hasvisible : false
     * previous_cursor : 0
     * next_cursor : 3904841749174397
     * total_number : 150
     * interval : 2000
     * uve_blank : -1
     * since_id : 3904859252596728
     * max_id : 3904841749174397
     * has_unread : 0
     */

    private boolean hasvisible;
    private int previous_cursor;
    private long next_cursor;
    private int total_number;
    private int interval;
    private int uve_blank;
    private long since_id;
    private long max_id;
    private int has_unread;
    /**
     * created_at : Mon Nov 02 22:27:36 +0800 2015
     * id : 3904859252596728
     * mid : 3904859252596728
     * idstr : 3904859252596728
     * text : 这个视频一点都不萌，就看了好几遍~[笑cry] (cr.梅梅盼达) http://t.cn/RUIlA32
     * source_allowclick : 0
     * source_type : 1
     * source : <a href="http://weibo.com/" rel="nofollow">微博 weibo.com</a>
     * favorited : false
     * truncated : false
     * in_reply_to_status_id :
     * in_reply_to_user_id :
     * in_reply_to_screen_name :
     * pic_urls : []
     * geo : null
     * user : {"id":1713926427,"idstr":"1713926427","class":1,"screen_name":"微博搞笑排行榜","name":"微博搞笑排行榜","province":"44","city":"1000","location":"广东","description":"微博搞笑中心！每天搜罗最搞笑最好玩的微博。关注我，获得每日新鲜笑料！（欢迎合作，投稿）↖(^ω^)↗","url":"http://weibo.com/mkdqs","profile_image_url":"http://tp4.sinaimg.cn/1713926427/50/40000875722/0","cover_image_phone":"http://ww3.sinaimg.cn/crop.0.0.640.640.640/6ce2240djw1e9oaaziu7sj20hs0hs0ui.jpg","profile_url":"topgirls8","domain":"topgirls8","weihao":"","gender":"f","followers_count":27158185,"friends_count":837,"pagefriends_count":0,"statuses_count":88408,"favourites_count":7056,"created_at":"Fri Mar 19 00:42:49 +0800 2010","following":true,"allow_all_act_msg":true,"geo_enabled":false,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/1713926427/180/40000875722/0","avatar_hd":"http://tp4.sinaimg.cn/1713926427/180/40000875722/0","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":297,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":37}
     * reposts_count : 10
     * comments_count : 20
     * attitudes_count : 46
     * mlevel : 0
     * visible : {"type":0,"list_id":0}
     * biz_ids : [230444]
     * biz_feature : 0
     * darwin_tags : []
     * rid : 0_0_1_2666885038303173078
     * userType : 0
     */

    private List<StatusesEntity> statuses;
    private List<?> advertises;
    private List<?> ad;

    public void setHasvisible(boolean hasvisible) {
        this.hasvisible = hasvisible;
    }

    public void setPrevious_cursor(int previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public void setNext_cursor(long next_cursor) {
        this.next_cursor = next_cursor;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setUve_blank(int uve_blank) {
        this.uve_blank = uve_blank;
    }

    public void setSince_id(long since_id) {
        this.since_id = since_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }

    public void setHas_unread(int has_unread) {
        this.has_unread = has_unread;
    }

    public void setStatuses(List<StatusesEntity> statuses) {
        this.statuses = statuses;
    }

    public void setAdvertises(List<?> advertises) {
        this.advertises = advertises;
    }

    public void setAd(List<?> ad) {
        this.ad = ad;
    }

    public boolean isHasvisible() {
        return hasvisible;
    }

    public int getPrevious_cursor() {
        return previous_cursor;
    }

    public long getNext_cursor() {
        return next_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public int getInterval() {
        return interval;
    }

    public int getUve_blank() {
        return uve_blank;
    }

    public long getSince_id() {
        return since_id;
    }

    public long getMax_id() {
        return max_id;
    }

    public int getHas_unread() {
        return has_unread;
    }

    public List<StatusesEntity> getStatuses() {
        return statuses;
    }

    public List<?> getAdvertises() {
        return advertises;
    }

    public List<?> getAd() {
        return ad;
    }

    public static class StatusesEntity {
        private String created_at;
        private long id;
        private String mid;
        private String idstr;
        private String text;
        private int source_allowclick;
        private int source_type;
        private String source;
        private boolean favorited;
        private boolean truncated;
        private String in_reply_to_status_id;
        private String in_reply_to_user_id;
        private String in_reply_to_screen_name;
        private Object geo;
        /**
         * id : 1713926427
         * idstr : 1713926427
         * class : 1
         * screen_name : 微博搞笑排行榜
         * name : 微博搞笑排行榜
         * province : 44
         * city : 1000
         * location : 广东
         * description : 微博搞笑中心！每天搜罗最搞笑最好玩的微博。关注我，获得每日新鲜笑料！（欢迎合作，投稿）↖(^ω^)↗
         * url : http://weibo.com/mkdqs
         * profile_image_url : http://tp4.sinaimg.cn/1713926427/50/40000875722/0
         * cover_image_phone : http://ww3.sinaimg.cn/crop.0.0.640.640.640/6ce2240djw1e9oaaziu7sj20hs0hs0ui.jpg
         * profile_url : topgirls8
         * domain : topgirls8
         * weihao :
         * gender : f
         * followers_count : 27158185
         * friends_count : 837
         * pagefriends_count : 0
         * statuses_count : 88408
         * favourites_count : 7056
         * created_at : Fri Mar 19 00:42:49 +0800 2010
         * following : true
         * allow_all_act_msg : true
         * geo_enabled : false
         * verified : false
         * verified_type : -1
         * remark :
         * ptype : 0
         * allow_all_comment : true
         * avatar_large : http://tp4.sinaimg.cn/1713926427/180/40000875722/0
         * avatar_hd : http://tp4.sinaimg.cn/1713926427/180/40000875722/0
         * verified_reason :
         * verified_trade :
         * verified_reason_url :
         * verified_source :
         * verified_source_url :
         * follow_me : false
         * online_status : 0
         * bi_followers_count : 297
         * lang : zh-cn
         * star : 0
         * mbtype : 12
         * mbrank : 6
         * block_word : 0
         * block_app : 1
         * credit_score : 80
         * user_ability : 0
         * urank : 37
         */

        private UserEntity user;
        private int reposts_count;
        private int comments_count;
        private int attitudes_count;
        private int mlevel;
        /**
         * type : 0
         * list_id : 0
         */

        private VisibleEntity visible;
        private long biz_feature;
        private String rid;
        private int userType;
        private List<Pic_urlsEntity> pic_urls;
        private List<Integer> biz_ids;
        private List<?> darwin_tags;

        private StatusesEntity retweeted_status;

        public static class Pic_urlsEntity{
            //默认返回缩略图
            //要得到高清图或者原图，把地址"http://ww1.sinaimg.cn/thumbnail/6dab804cjw1exv392snomj21kw23ukjl.jpg"
            //中的thumbnail换成对应的bmiddle(高清)或者large(原图)
            private String thumbnail_pic;

            @Override
            public String toString() {
                return "Pic_urlsEntity{" +
                        "thumbnail_pic='" + thumbnail_pic + '\'' +
                        '}';
            }

            public String getThumbnail_pic() {
                return thumbnail_pic;
            }

            public void setThumbnail_pic(String thumbnail_pic) {
                this.thumbnail_pic = thumbnail_pic;
            }
        }


        public StatusesEntity getRetweeted_status() {
            return retweeted_status;
        }

        public void setRetweeted_status(StatusesEntity retweeted_status) {
            this.retweeted_status = retweeted_status;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public void setIdstr(String idstr) {
            this.idstr = idstr;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setSource_allowclick(int source_allowclick) {
            this.source_allowclick = source_allowclick;
        }

        public void setSource_type(int source_type) {
            this.source_type = source_type;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setFavorited(boolean favorited) {
            this.favorited = favorited;
        }

        public void setTruncated(boolean truncated) {
            this.truncated = truncated;
        }

        public void setIn_reply_to_status_id(String in_reply_to_status_id) {
            this.in_reply_to_status_id = in_reply_to_status_id;
        }

        public void setIn_reply_to_user_id(String in_reply_to_user_id) {
            this.in_reply_to_user_id = in_reply_to_user_id;
        }

        public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
            this.in_reply_to_screen_name = in_reply_to_screen_name;
        }

        public void setGeo(Object geo) {
            this.geo = geo;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public void setReposts_count(int reposts_count) {
            this.reposts_count = reposts_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public void setAttitudes_count(int attitudes_count) {
            this.attitudes_count = attitudes_count;
        }

        public void setMlevel(int mlevel) {
            this.mlevel = mlevel;
        }

        public void setVisible(VisibleEntity visible) {
            this.visible = visible;
        }

        public void setBiz_feature(long biz_feature) {
            this.biz_feature = biz_feature;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public void setPic_urls(List<Pic_urlsEntity> pic_urls) {
            this.pic_urls = pic_urls;
        }

        public void setBiz_ids(List<Integer> biz_ids) {
            this.biz_ids = biz_ids;
        }

        public void setDarwin_tags(List<?> darwin_tags) {
            this.darwin_tags = darwin_tags;
        }

        public String getCreated_at() {
            return created_at;
        }

        public long getId() {
            return id;
        }

        public String getMid() {
            return mid;
        }

        public String getIdstr() {
            return idstr;
        }

        public String getText() {
            return text;
        }

        public int getSource_allowclick() {
            return source_allowclick;
        }

        public int getSource_type() {
            return source_type;
        }

        public String getSource() {
            return source;
        }

        public boolean isFavorited() {
            return favorited;
        }

        public boolean isTruncated() {
            return truncated;
        }

        public String getIn_reply_to_status_id() {
            return in_reply_to_status_id;
        }

        public String getIn_reply_to_user_id() {
            return in_reply_to_user_id;
        }

        public String getIn_reply_to_screen_name() {
            return in_reply_to_screen_name;
        }

        public Object getGeo() {
            return geo;
        }

        public UserEntity getUser() {
            return user;
        }

        public int getReposts_count() {
            return reposts_count;
        }

        public int getComments_count() {
            return comments_count;
        }

        public int getAttitudes_count() {
            return attitudes_count;
        }

        public int getMlevel() {
            return mlevel;
        }

        public VisibleEntity getVisible() {
            return visible;
        }

        public long getBiz_feature() {
            return biz_feature;
        }

        public String getRid() {
            return rid;
        }

        public int getUserType() {
            return userType;
        }

        public List<Pic_urlsEntity> getPic_urls() {
            return pic_urls;
        }

        public List<Integer> getBiz_ids() {
            return biz_ids;
        }

        public List<?> getDarwin_tags() {
            return darwin_tags;
        }

        public static class UserEntity {
            private long id;
            private String idstr;
            @SerializedName("class")
            private int classX;
            private String screen_name;
            private String name;
            private String province;
            private String city;
            private String location;
            private String description;
            private String url;
            private String profile_image_url;
            private String cover_image_phone;
            private String profile_url;
            private String domain;
            private String weihao;
            private String gender;
            private int followers_count;
            private int friends_count;
            private int pagefriends_count;
            private int statuses_count;
            private int favourites_count;
            private String created_at;
            private boolean following;
            private boolean allow_all_act_msg;
            private boolean geo_enabled;
            private boolean verified;
            private int verified_type;
            private String remark;
            private int ptype;
            private boolean allow_all_comment;
            private String avatar_large;
            private String avatar_hd;
            private String verified_reason;
            private String verified_trade;
            private String verified_reason_url;
            private String verified_source;
            private String verified_source_url;
            private boolean follow_me;
            private int online_status;
            private int bi_followers_count;
            private String lang;
            private int star;
            private int mbtype;
            private int mbrank;
            private int block_word;
            private int block_app;
            private int credit_score;
            private int user_ability;
            private int urank;

            public void setId(long id) {
                this.id = id;
            }

            public void setIdstr(String idstr) {
                this.idstr = idstr;
            }

            public void setClassX(int classX) {
                this.classX = classX;
            }

            public void setScreen_name(String screen_name) {
                this.screen_name = screen_name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setProfile_image_url(String profile_image_url) {
                this.profile_image_url = profile_image_url;
            }

            public void setCover_image_phone(String cover_image_phone) {
                this.cover_image_phone = cover_image_phone;
            }

            public void setProfile_url(String profile_url) {
                this.profile_url = profile_url;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public void setWeihao(String weihao) {
                this.weihao = weihao;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public void setFollowers_count(int followers_count) {
                this.followers_count = followers_count;
            }

            public void setFriends_count(int friends_count) {
                this.friends_count = friends_count;
            }

            public void setPagefriends_count(int pagefriends_count) {
                this.pagefriends_count = pagefriends_count;
            }

            public void setStatuses_count(int statuses_count) {
                this.statuses_count = statuses_count;
            }

            public void setFavourites_count(int favourites_count) {
                this.favourites_count = favourites_count;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public void setFollowing(boolean following) {
                this.following = following;
            }

            public void setAllow_all_act_msg(boolean allow_all_act_msg) {
                this.allow_all_act_msg = allow_all_act_msg;
            }

            public void setGeo_enabled(boolean geo_enabled) {
                this.geo_enabled = geo_enabled;
            }

            public void setVerified(boolean verified) {
                this.verified = verified;
            }

            public void setVerified_type(int verified_type) {
                this.verified_type = verified_type;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public void setPtype(int ptype) {
                this.ptype = ptype;
            }

            public void setAllow_all_comment(boolean allow_all_comment) {
                this.allow_all_comment = allow_all_comment;
            }

            public void setAvatar_large(String avatar_large) {
                this.avatar_large = avatar_large;
            }

            public void setAvatar_hd(String avatar_hd) {
                this.avatar_hd = avatar_hd;
            }

            public void setVerified_reason(String verified_reason) {
                this.verified_reason = verified_reason;
            }

            public void setVerified_trade(String verified_trade) {
                this.verified_trade = verified_trade;
            }

            public void setVerified_reason_url(String verified_reason_url) {
                this.verified_reason_url = verified_reason_url;
            }

            public void setVerified_source(String verified_source) {
                this.verified_source = verified_source;
            }

            public void setVerified_source_url(String verified_source_url) {
                this.verified_source_url = verified_source_url;
            }

            public void setFollow_me(boolean follow_me) {
                this.follow_me = follow_me;
            }

            public void setOnline_status(int online_status) {
                this.online_status = online_status;
            }

            public void setBi_followers_count(int bi_followers_count) {
                this.bi_followers_count = bi_followers_count;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public void setMbtype(int mbtype) {
                this.mbtype = mbtype;
            }

            public void setMbrank(int mbrank) {
                this.mbrank = mbrank;
            }

            public void setBlock_word(int block_word) {
                this.block_word = block_word;
            }

            public void setBlock_app(int block_app) {
                this.block_app = block_app;
            }

            public void setCredit_score(int credit_score) {
                this.credit_score = credit_score;
            }

            public void setUser_ability(int user_ability) {
                this.user_ability = user_ability;
            }

            public void setUrank(int urank) {
                this.urank = urank;
            }

            public long getId() {
                return id;
            }

            public String getIdstr() {
                return idstr;
            }

            public int getClassX() {
                return classX;
            }

            public String getScreen_name() {
                return screen_name;
            }

            public String getName() {
                return name;
            }

            public String getProvince() {
                return province;
            }

            public String getCity() {
                return city;
            }

            public String getLocation() {
                return location;
            }

            public String getDescription() {
                return description;
            }

            public String getUrl() {
                return url;
            }

            public String getProfile_image_url() {
                return profile_image_url;
            }

            public String getCover_image_phone() {
                return cover_image_phone;
            }

            public String getProfile_url() {
                return profile_url;
            }

            public String getDomain() {
                return domain;
            }

            public String getWeihao() {
                return weihao;
            }

            public String getGender() {
                return gender;
            }

            public int getFollowers_count() {
                return followers_count;
            }

            public int getFriends_count() {
                return friends_count;
            }

            public int getPagefriends_count() {
                return pagefriends_count;
            }

            public int getStatuses_count() {
                return statuses_count;
            }

            public int getFavourites_count() {
                return favourites_count;
            }

            public String getCreated_at() {
                return created_at;
            }

            public boolean isFollowing() {
                return following;
            }

            public boolean isAllow_all_act_msg() {
                return allow_all_act_msg;
            }

            public boolean isGeo_enabled() {
                return geo_enabled;
            }

            public boolean isVerified() {
                return verified;
            }

            public int getVerified_type() {
                return verified_type;
            }

            public String getRemark() {
                return remark;
            }

            public int getPtype() {
                return ptype;
            }

            public boolean isAllow_all_comment() {
                return allow_all_comment;
            }

            public String getAvatar_large() {
                return avatar_large;
            }

            public String getAvatar_hd() {
                return avatar_hd;
            }

            public String getVerified_reason() {
                return verified_reason;
            }

            public String getVerified_trade() {
                return verified_trade;
            }

            public String getVerified_reason_url() {
                return verified_reason_url;
            }

            public String getVerified_source() {
                return verified_source;
            }

            public String getVerified_source_url() {
                return verified_source_url;
            }

            public boolean isFollow_me() {
                return follow_me;
            }

            public int getOnline_status() {
                return online_status;
            }

            public int getBi_followers_count() {
                return bi_followers_count;
            }

            public String getLang() {
                return lang;
            }

            public int getStar() {
                return star;
            }

            public int getMbtype() {
                return mbtype;
            }

            public int getMbrank() {
                return mbrank;
            }

            public int getBlock_word() {
                return block_word;
            }

            public int getBlock_app() {
                return block_app;
            }

            public int getCredit_score() {
                return credit_score;
            }

            public int getUser_ability() {
                return user_ability;
            }

            public int getUrank() {
                return urank;
            }
        }

        public static class VisibleEntity {
            private int type;
            private int list_id;

            public void setType(int type) {
                this.type = type;
            }

            public void setList_id(int list_id) {
                this.list_id = list_id;
            }

            public int getType() {
                return type;
            }

            public int getList_id() {
                return list_id;
            }
        }
    }
}
