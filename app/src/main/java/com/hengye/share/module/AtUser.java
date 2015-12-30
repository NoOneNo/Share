package com.hengye.share.module;

import android.text.TextUtils;

import com.hengye.share.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class AtUser extends Select{

    private String username;

    public AtUser() {
    }

    public AtUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static List<AtUser> search(List<AtUser> targets, String str) {
        List<AtUser> result = new ArrayList<>();
        if(CommonUtil.isEmptyCollection(targets)){
            return result;
        }
        for(AtUser au : targets){
            if(!TextUtils.isEmpty(au.getUsername())){
                if(au.getUsername().contains(str)){
                    result.add(au);
                }
            }
        }
        return result;
    }
}
