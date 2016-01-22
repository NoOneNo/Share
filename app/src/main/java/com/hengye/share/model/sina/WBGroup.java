package com.hengye.share.model.sina;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WBGroup {

    public final static int TYPE_PRIVATE = 0;
    public final static int TYPE_SYSTEM= 1;
    public final static int TYPE_CUSTOM= 2;

    private long id;
    private String idstr;
    private String name;
    private String mode;
    private int visible;
    private int like_count;
    private int member_count;
    private String description;
    private String profile_image_url;

    private WBUserInfo user;
    private String created_at;
    private List<?> tags;

    public int getGroypType(){
        if("system".equals(mode)){
            return TYPE_SYSTEM;
        }else if("private".equals(mode)){
            return TYPE_PRIVATE;
        }
        return TYPE_CUSTOM;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public void setUser(WBUserInfo user) {
        this.user = user;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setTags(List<?> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public String getIdstr() {
        return idstr;
    }

    public String getName() {
        return name;
    }

    public String getMode() {
        return mode;
    }

    public int getVisible() {
        return visible;
    }

    public int getLike_count() {
        return like_count;
    }

    public int getMember_count() {
        return member_count;
    }

    public String getDescription() {
        return description;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public WBUserInfo getUser() {
        return user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public List<?> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "WBGroup{" +
                "id=" + id +
                ", idstr='" + idstr + '\'' +
                ", name='" + name + '\'' +
                ", mode='" + mode + '\'' +
                ", visible=" + visible +
                ", like_count=" + like_count +
                ", member_count=" + member_count +
                ", description='" + description + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", user=" + user +
                ", created_at='" + created_at + '\'' +
                ", tags=" + tags +
                '}';
    }
}
