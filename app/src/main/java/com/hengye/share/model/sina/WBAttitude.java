package com.hengye.share.model.sina;

/**
 * Created by yuhy on 2017/2/14.
 */

public class WBAttitude {

    /**
     * id : 4075133700343679
     * created_at : Tue Feb 14 19:17:29 +0800 2017
     * attitude : smile
     * attitude_type : 0
     * last_attitude :
     * source_allowclick : 0
     * source_type : 1
     * source : <a href="http://app.weibo.com/t/feed/l4RWD" rel="nofollow">Weico.Android</a>
     */

    private long id;
    private String created_at;
    private String attitude;
    private int attitude_type;
    private String last_attitude;
    private int source_allowclick;
    private int source_type;
    private String source;

    private WBUserInfo user;
    private WBTopic status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    public int getAttitude_type() {
        return attitude_type;
    }

    public void setAttitude_type(int attitude_type) {
        this.attitude_type = attitude_type;
    }

    public String getLast_attitude() {
        return last_attitude;
    }

    public void setLast_attitude(String last_attitude) {
        this.last_attitude = last_attitude;
    }

    public int getSource_allowclick() {
        return source_allowclick;
    }

    public void setSource_allowclick(int source_allowclick) {
        this.source_allowclick = source_allowclick;
    }

    public int getSource_type() {
        return source_type;
    }

    public void setSource_type(int source_type) {
        this.source_type = source_type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public WBUserInfo getUser() {
        return user;
    }

    public void setUser(WBUserInfo user) {
        this.user = user;
    }

    public WBTopic getStatus() {
        return status;
    }

    public void setStatus(WBTopic status) {
        this.status = status;
    }
}
