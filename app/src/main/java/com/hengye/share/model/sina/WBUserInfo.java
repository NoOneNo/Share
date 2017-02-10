package com.hengye.share.model.sina;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WBUserInfo implements Serializable{

    private static final long serialVersionUID = -6328937740278034345L;
    /**
     * id : 2207519004
     * idstr : 2207519004
     * class : 1
     * screen_name : 我是一只小小小鸡仔
     * name : 我是一只小小小鸡仔
     * province : 44
     * city : 9
     * location : 广东 茂名
     * description : 捉紧 生命浓度 坦白流露 感情和态度。
     * url :
     * profile_image_url : http://tp1.sinaimg.cn/2207519004/50/5716984948/1
     * profile_url : u/2207519004
     * domain :
     * weihao :
     * gender : m
     * followers_count : 26
     * friends_count : 84
     * pagefriends_count : 1
     * statuses_count : 156
     * favourites_count : 42
     * created_at : Mon Aug 15 15:01:03 +0800 2011
     * following : false
     * allow_all_act_msg : false
     * geo_enabled : true
     * verified : false
     * verified_type : -1
     * remark :
     * status : {"created_at":"Mon Oct 19 01:08:38 +0800 2015","id":3899463959387300,"mid":"3899463959387300","idstr":"3899463959387300","text":"抱歉，此微博已被作者删除。查看帮助：http://t.cn/zWSudZc","deleted":"1"}
     * ptype : 0
     * allow_all_comment : true
     * avatar_large : http://tp1.sinaimg.cn/2207519004/180/5716984948/1
     * avatar_hd : http://ww2.sinaimg.cn/crop.0.0.1152.1152.1024/8394111cjw8eojqxo0z37j20w00w077j.jpg
     * verified_reason :
     * verified_trade :
     * verified_reason_url :
     * verified_source :
     * verified_source_url :
     * follow_me : false
     * online_status : 0
     * bi_followers_count : 11
     * lang : zh-cn
     * star : 0
     * mbtype : 0
     * mbrank : 0
     * block_word : 0
     * block_app : 0
     * credit_score : 80
     * user_ability : 0
     * urank : 14
     */

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
    private String profile_url;
    private String cover_image_phone;
    private String domain;
    private String weihao;
    private String gender;
    private long followers_count;
    private long friends_count;
    private long follow_count;//微博移动端用此字段表博士关注数量
    private long pagefriends_count;
    private long statuses_count;
    private long favourites_count;
    private String created_at;
    private boolean following;
    private boolean allow_all_act_msg;
    private boolean geo_enabled;
    private boolean verified;
    private int verified_type;
    private String remark;
    /**
     * created_at : Mon Oct 19 01:08:38 +0800 2015
     * id : 3899463959387300
     * mid : 3899463959387300
     * idstr : 3899463959387300
     * text : 抱歉，此微博已被作者删除。查看帮助：http://t.cn/zWSudZc
     * deleted : 1
     */

    private WBTopic status;
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
    private long star;
    private int mbtype;
    private int mbrank;
    private int block_word;
    private int block_app;
    private int credit_score;
    private int user_ability;
    private int urank;

    public String getCover_image_phone() {
        return cover_image_phone;
    }

    public void setCover_image_phone(String cover_image_phone) {
        this.cover_image_phone = cover_image_phone;
    }

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

    public void setFollowers_count(long followers_count) {
        this.followers_count = followers_count;
    }

    public void setFriends_count(long friends_count) {
        this.friends_count = friends_count;
    }

    public void setFollow_count(long follow_count) {
        this.follow_count = follow_count;
    }

    public void setPagefriends_count(long pagefriends_count) {
        this.pagefriends_count = pagefriends_count;
    }

    public void setStatuses_count(long statuses_count) {
        this.statuses_count = statuses_count;
    }

    public void setFavourites_count(long favourites_count) {
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

    public void setStatus(WBTopic status) {
        this.status = status;
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

    public void setStar(long star) {
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

    public long getFollowers_count() {
        return followers_count;
    }

    public long getFriends_count() {
        return friends_count;
    }

    public long getFollow_count() {
        return follow_count;
    }

    public long getPagefriends_count() {
        return pagefriends_count;
    }

    public long getStatuses_count() {
        return statuses_count;
    }

    public long getFavourites_count() {
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

    public WBTopic getStatus() {
        return status;
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

    public long getStar() {
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
