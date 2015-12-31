package com.hengye.share.module.sina;

import java.util.List;

public class WBUserInfos {

    private int next_cursor;
    private int previous_cursor;
    private int total_number;

    private List<WBUserInfo> users;

    public void setNext_cursor(int next_cursor) {
        this.next_cursor = next_cursor;
    }

    public void setPrevious_cursor(int previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public void setUsers(List<WBUserInfo> users) {
        this.users = users;
    }

    public int getNext_cursor() {
        return next_cursor;
    }

    public int getPrevious_cursor() {
        return previous_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public List<WBUserInfo> getUsers() {
        return users;
    }


}
