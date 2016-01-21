package com.hengye.share.util;

import com.hengye.share.model.Parent;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.GreenDaoManager;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.greenrobot.UserDao;
import com.hengye.share.model.sina.WBUserInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.List;

public class UserUtil {

    private static User mCurrentUser;

    public static User getCurrentUser() {
        if (mCurrentUser == null) {
            mCurrentUser = getDefaultUser();
        }
        return mCurrentUser;
    }

    public static User updateCurrentUser() {
        return mCurrentUser = getDefaultUser();
    }

    public static String getUid() {
        if (getCurrentUser() == null) {
            return null;
        } else {
            return getCurrentUser().getUid();
        }
    }

    public static String getToken() {
        if (getCurrentUser() == null) {
            return null;
        } else {
            return getCurrentUser().getToken();
        }
    }

    public static boolean isUserEmpty() {
        return getCurrentUser() == null;
    }

    public static boolean isUserNameEmpty() {
        return getCurrentUser() == null || getCurrentUser().getName() == null;
    }

    public static boolean isTokenEmpty() {
        return getToken() == null;
    }

    public static User getDefaultUser() {
        User user = null;
        String uid = SPUtil.getUid();
        if (uid != null) {
            UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
            List<User> users = ud.queryRaw("where UID = ?", uid);
            if (!CommonUtil.isEmptyCollection(users)) {
                user =  users.get(0);
            }
        }

       if(user == null){
           UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
           List<User> users = ud.queryRaw("limit 1");
           if (!CommonUtil.isEmptyCollection(users)) {
               user =  users.get(0);
           }
       }
        return user;
    }

    public static void updateUser(Oauth2AccessToken accessToken) {
        User user;
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        List<User> users = ud.queryRaw("where UID = ? and PARENT_TYPE = ?", accessToken.getUid(), Parent.TYPE_WEIBO + "");
        if (CommonUtil.isEmptyCollection(users)) {
            user = new User();
            user.setUid(accessToken.getUid());
            user.setToken(accessToken.getToken());
            user.setRefreshToken(accessToken.getRefreshToken());
            user.setExpiresIn(accessToken.getExpiresTime());
            user.setParentType(Parent.TYPE_WEIBO);
            ud.insert(user);
        } else {
            user = users.get(0);
            user.setToken(accessToken.getToken());
            user.setRefreshToken(accessToken.getRefreshToken());
            user.setExpiresIn(accessToken.getExpiresTime());
            ud.update(user);
        }

        mCurrentUser = user;
        SPUtil.setUid(user.getUid());
    }

    public static void updateUserInfo(WBUserInfo wbUserInfo) {
        User user;
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        List<User> users = ud.queryRaw("where UID = ? and PARENT_TYPE = ?", wbUserInfo.getIdstr(), Parent.TYPE_WEIBO + "");
        if (!CommonUtil.isEmptyCollection(users)) {
            user = users.get(0);
            user.setName(wbUserInfo.getName());
            user.setAvatar(wbUserInfo.getAvatar_large());
            user.setCover(wbUserInfo.getCover_image_phone());
            user.setGender(wbUserInfo.getGender());
            user.setSign(wbUserInfo.getDescription());
            user.setParentJson(GsonUtil.getInstance().toJson(wbUserInfo));
            ud.update(user);
        }
    }

    public static void updateUser(User targetUser){
        User user;
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        List<User> users = ud.queryRaw("where UID = ?", targetUser.getUid());
        if(!CommonUtil.isEmptyCollection(users)){
            user = users.get(0);
            targetUser.setId(user.getId());
            ud.update(targetUser);
        }
    }

}
