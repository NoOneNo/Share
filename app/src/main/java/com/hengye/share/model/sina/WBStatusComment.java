package com.hengye.share.model.sina;

public class WBStatusComment {
    private String created_at;
    private long id;
    private String text;
    private int source_allowclick;
    private int source_type;
    private String source;

    private WBUserInfo user;
    private String mid;
    private String idstr;
    private WBStatus status;

    private WBStatusComment reply_comment;

    private int floor_num;

    private boolean liked;//是否已经点赞;
    private long like_counts;//点赞数

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setUser(WBUserInfo user) {
        this.user = user;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public void setStatus(WBStatus status) {
        this.status = status;
    }

    public void setReply_comment(WBStatusComment reply_comment) {
        this.reply_comment = reply_comment;
    }

    public void setFloor_num(int floor_num) {
        this.floor_num = floor_num;
    }

    public String getCreated_at() {
        return created_at;
    }

    public long getId() {
        return id;
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

    public WBUserInfo getUser() {
        return user;
    }

    public String getMid() {
        return mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public WBStatus getStatus() {
        return status;
    }

    public WBStatusComment getReply_comment() {
        return reply_comment;
    }

    public int getFloor_num() {
        return floor_num;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public long getLike_counts() {
        return like_counts;
    }

    public void setLike_counts(long like_counts) {
        this.like_counts = like_counts;
    }

    @Override
    public String toString() {
        return "WBStatusComment{" +
                "created_at='" + created_at + '\'' +
                ", id=" + id +
                ", text='" + text + '\'' +
                ", source_allowclick=" + source_allowclick +
                ", source_type=" + source_type +
                ", source='" + source + '\'' +
                ", user=" + user +
                ", mid='" + mid + '\'' +
                ", idstr='" + idstr + '\'' +
                ", status=" + status +
                ", reply_comment=" + reply_comment +
                ", floor_num=" + floor_num +
                ", liked=" + liked +
                ", like_counts=" + like_counts +
                '}';
    }
}
