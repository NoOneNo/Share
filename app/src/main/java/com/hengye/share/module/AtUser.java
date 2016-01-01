package com.hengye.share.module;

import android.text.TextUtils;

import com.hengye.share.util.CommonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AtUser extends Select implements Serializable{

    private static final long serialVersionUID = -4261232745907154037L;

    private boolean isPrepareDelete;

    private UserInfo userInfo;

    public AtUser() {
    }

    public AtUser(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static String getFormatAtUserName(List<String> username){
        StringBuilder sb = new StringBuilder();
        for(String str : username){
            sb.append("@");
            sb.append(str);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static ArrayList<String> getAtUserName(List<AtUser> atUsers) {
        ArrayList<String> temp = new ArrayList<>();
        for(AtUser au : atUsers){
            UserInfo userInfo = au.getUserInfo();
            if(userInfo != null) {
                temp.add(userInfo.getName());
            }
        }
        return temp;
    }

    public static ArrayList<AtUser> getAtUser(List<UserInfo> userInfos) {
        ArrayList<AtUser> temp = new ArrayList<>();
        if (CommonUtil.isEmptyCollection(userInfos)) {
            return temp;
        }

        for (UserInfo userInfo : userInfos) {
            temp.add(new AtUser(userInfo));
        }
        return temp;
    }

    public static ArrayList<AtUser> search(List<AtUser> targets, String str) {
        ArrayList<AtUser> result = new ArrayList<>();
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
