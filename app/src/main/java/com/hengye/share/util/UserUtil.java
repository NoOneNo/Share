package com.hengye.share.util;

import com.hengye.share.model.Parent;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.GreenDaoManager;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.greenrobot.UserDao;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.List;

public class UserUtil {

    private static User mCurrentUser;

    public static User getCurrentUser(){
        if(mCurrentUser == null){
//            mCurrentUser = getDefaultUser();
        }
        return mCurrentUser;
    }

//    public static User getDefaultUser(){
//        SPUtil.getSinaUid()
//    }

    public static void updateUser(Oauth2AccessToken accessToken){
        User user;
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        List<User> users = ud.queryRaw("where uid = ? and parentType = ?", accessToken.getUid(), Parent.TYPE_WEIBO + "");
        if(CommonUtil.isEmptyCollection(users)){
            user = new User();
            user.setUid(accessToken.getUid());
            user.setToken(accessToken.getToken());
            user.setRefreshToken(accessToken.getRefreshToken());
            user.setExpiresIn(accessToken.getExpiresTime());
            user.setParentType(Parent.TYPE_WEIBO);
            ud.insert(user);
        }else{
            user = users.get(0);
            user.setToken(accessToken.getToken());
            user.setRefreshToken(accessToken.getRefreshToken());
            user.setExpiresIn(accessToken.getExpiresTime());
            ud.update(user);
        }

        mCurrentUser = user;
    }

    public static void updateUserInfo(String uid, UserInfo userInfo, int parentType){
        User user;
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        List<User> users = ud.queryRaw("where uid = ? and parentType = ?", uid, parentType + "");
        if(!CommonUtil.isEmptyCollection(users)){
            user = users.get(0);
            user.setName(userInfo.getName());
            user.setAvatar(userInfo.getAvatar());
            user.setCover(userInfo.getCover());
            user.setGender(userInfo.getGender());
            user.setSign(userInfo.getSign());
            ud.update(user);
        }
    }


}
