package com.hengye.share.model.sina;

import java.util.List;

/**
 * Created by yuhy on 2017/2/14.
 */

public class WBAttitudes {

    List<WBUserInfo> users;

    long total_number;

    public List<WBUserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<WBUserInfo> users) {
        this.users = users;
    }

    public long getTotal_number() {
        return total_number;
    }

    public void setTotal_number(long total_number) {
        this.total_number = total_number;
    }
}
