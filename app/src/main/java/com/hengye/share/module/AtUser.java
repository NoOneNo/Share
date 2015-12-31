package com.hengye.share.module;

import android.text.TextUtils;

import com.hengye.share.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class AtUser extends Select {

//    private String username;

    private boolean isPrepareDelete;

    private UserInfo userInfo;

    public AtUser() {
    }

    public AtUser(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
//    public AtUser(String username) {
//        this.username = username;
//    }

    public static List<AtUser> getAtUser(List<UserInfo> userInfos) {
        List<AtUser> temp = new ArrayList<>();
        if (CommonUtil.isEmptyCollection(userInfos)) {
            return temp;
        }

        for (UserInfo userInfo : userInfos) {
            temp.add(new AtUser(userInfo));
        }
        return temp;
    }

    public static List<AtUser> search(List<AtUser> targets, String str) {
        List<AtUser> result = new ArrayList<>();
        if (CommonUtil.isEmptyCollection(targets)) {
            return result;
        }
        for (AtUser au : targets) {
            if (au.getUserInfo() != null) {
                if (!TextUtils.isEmpty(au.getUserInfo().getName())) {
                    if (au.getUserInfo().getName().contains(str)) {
                        result.add(au);
                    }
                }
            }
        }
        return result;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isPrepareDelete() {
        return isPrepareDelete;
    }

    public void setPrepareDelete(boolean prepareDelete) {
        isPrepareDelete = prepareDelete;
    }
}
