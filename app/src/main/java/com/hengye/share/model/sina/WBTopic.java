package com.hengye.share.model.sina;

import java.io.Serializable;
import java.util.List;

public class WBTopic implements Serializable{

    private static final long serialVersionUID = 3472592334630262158L;
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

    private WBUserInfo user;
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

    private WBTopic retweeted_status;


    private String deleted;//1代表微博已删除

    public static class Pic_urlsEntity implements Serializable{

        private static final long serialVersionUID = 4005756573688221817L;

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


    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public WBTopic getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(WBTopic retweeted_status) {
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

    public void setUser(WBUserInfo user) {
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

    public WBUserInfo getUser() {
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

    public static class VisibleEntity implements Serializable{

        private static final long serialVersionUID = -5270600301367680541L;

        private int type;
        private String list_id;

        public void setType(int type) {
            this.type = type;
        }

        public void setList_id(String list_id) {
            this.list_id = list_id;
        }

        public int getType() {
            return type;
        }

        public String getList_id() {
            return list_id;
        }
    }
}
