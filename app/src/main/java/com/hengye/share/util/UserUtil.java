package com.hengye.share.util;

import android.graphics.Bitmap;

import com.hengye.share.model.Parent;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.GreenDaoManager;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.greenrobot.GroupListDao;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.greenrobot.UserDao;
import com.hengye.share.model.sina.WBUserInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.List;

import org.greenrobot.greendao.query.QueryBuilder;

public class UserUtil {

    private static User mCurrentUser;

    public static User getCurrentUser() {
        if (mCurrentUser == null) {
            mCurrentUser = getDefaultUser();
        }
        if (mCurrentUser == null) {
            mCurrentUser = new User();
            mCurrentUser.setUid("");//如果为NULL插入数据库会报错
        }
        return mCurrentUser;
    }

    private static User updateCurrentUser() {
        return mCurrentUser = getDefaultUser();
    }

    public static void changeCurrentUser(User user) {
        SPUtil.setUid(user == null ? null : user.getUid());
        mCurrentUser = user;
    }

    public static void deleteUser(User user) {
        if (mCurrentUser != null && mCurrentUser.equals(user)) {
            mCurrentUser = null;
            SPUtil.setUid(null);
        }
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        ud.delete(user);
    }

    public static UserInfo getCurrentUserInfo(){
        return UserInfo.getUserInfo(getCurrentUser());
    }

    public static String getUid() {
        return getCurrentUser().getUid();
    }

    public static String getName() {
        return getCurrentUser().getName();
    }

    public static String getToken() {
        return getCurrentUser().getToken();
    }

    public static String getAdToken() {
        return getCurrentUser().getAdToken();
    }

    public static String getPriorToken() {
        String token = getAdToken();
        if (token != null) {
            return token;
        }
        return getToken();
    }

    public static Bitmap getUserAvatarBitmap() {
        return RequestManager.getBitmapByUrl(getCurrentUser().getAvatar());
    }

    public static boolean isUserEmpty() {
        return isTokenEmpty() || isUidEmpty();
    }

    public static boolean isUidEmpty() {
        return CommonUtil.isEmpty(getUid());
    }

    public static boolean isUserNameEmpty() {
        return getCurrentUser().getName() == null;
    }

    public static boolean isTokenEmpty() {
        return getToken() == null;
    }

    public static boolean isAdTokenEmpty() {
        return getAdToken() == null;
    }

    public static boolean isCurrentUser(String uid){
        return !isUidEmpty() && getUid().equals(uid);
    }

    public static void resetToken() {
        if(isUidEmpty()){
            return;
        }
        User user = getCurrentUser();
        user.setToken("");//token不能为空，如果过期则改为空字符串
        user.setAdToken(null);
        updateUser(user);
    }

    public static void clearCookie() {
        User user = getCurrentUser();
        user.setCookie(null);
        UserUtil.updateUser(user);
    }

    private static User getDefaultUser() {
        User user = queryUser(SPUtil.getUid());
        if (user == null) {
            UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
            List<User> users = ud
                    .queryBuilder()
                    .limit(1)
                    .list();
//           List<User> users = ud.queryRaw("limit 1");
            if (!CommonUtil.isEmpty(users)) {
                user = users.get(0);
            }
        }
        return user;
    }

    public static User getUser(String uid) {
        if(uid != null){
            return uid.equals(UserUtil.getUid()) ?
                    UserUtil.getCurrentUser() : UserUtil.queryUser(uid);
        }
        return null;
    }

    private static User queryUser(String uid) {
        User user = null;
        if (uid != null) {
            UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
            List<User> users = ud
                    .queryBuilder()
                    .where(UserDao.Properties.Uid.eq(uid))
                    .list();
//            List<User> users = ud.queryRaw("where UID = ?", uid);
            if (!CommonUtil.isEmpty(users)) {
                user = users.get(0);
            }
        }
        return user;
    }

    public static void updateUser(Oauth2AccessToken accessToken) {
        updateUser(accessToken, null, null);
    }

    public static void updateUser(Oauth2AccessToken accessToken, String account, String password) {
        User user;
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        List<User> users = ud
                .queryBuilder()
                .where(UserDao.Properties.Uid.eq(accessToken.getUid())
                        , UserDao.Properties.ParentType.eq(Parent.TYPE_WEIBO))
                .list();
//        List<User> users = ud.queryRaw("where UID = ? and PARENT_TYPE = ?", accessToken.getUid(), Parent.TYPE_WEIBO + "");
        if (CommonUtil.isEmpty(users)) {
            user = new User();
            user.setParentType(Parent.TYPE_WEIBO);
            user.setUid(accessToken.getUid());
            user.setToken(accessToken.getToken());
            user.setRefreshToken(accessToken.getRefreshToken());
            user.setExpiresIn(accessToken.getExpiresTime());
            if (CommonUtil.noEmpty(account, password)) {
                user.setAccount(account);
                user.setPassword(password);
            }
            ud.insert(user);
        } else {
            user = users.get(0);
            user.setToken(accessToken.getToken());
            user.setRefreshToken(accessToken.getRefreshToken());
            user.setExpiresIn(accessToken.getExpiresTime());
            if (CommonUtil.noEmpty(account, password)) {
                user.setAccount(account);
                user.setPassword(password);
            }
            ud.update(user);
        }

        //更改当前用户
        mCurrentUser = user;
        SPUtil.setUid(user.getUid());
    }

    public static void updateUserInfo(WBUserInfo wbUserInfo) {
        User user;
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        List<User> users = ud
                .queryBuilder()
                .where(UserDao.Properties.Uid.eq(wbUserInfo.getIdstr())
                        , UserDao.Properties.ParentType.eq(Parent.TYPE_WEIBO))
                .list();
//        List<User> users = ud.queryRaw("where UID = ? and PARENT_TYPE = ?", wbUserInfo.getIdstr(), Parent.TYPE_WEIBO + "");
        if (!CommonUtil.isEmpty(users)) {
            user = users.get(0);
            user.setName(wbUserInfo.getScreen_name());
            user.setAvatar(wbUserInfo.getAvatar_large());
            user.setCover(wbUserInfo.getCover_image_phone());
            user.setGender(wbUserInfo.getGender());
            user.setSign(wbUserInfo.getDescription());
            user.setParentJson(GsonUtil.toJson(wbUserInfo));
            ud.update(user);
        }
    }

    public static void updateUser(User targetUser) {
        if(targetUser.getUid() == null){
            return;
        }
        User user;
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        List<User> users = ud.queryBuilder().where(UserDao.Properties.Uid.eq(targetUser.getUid())).list();
        if (!CommonUtil.isEmpty(users)) {
            user = users.get(0);
            targetUser.setId(user.getId());
            ud.update(targetUser);

            if (getCurrentUser().equals(targetUser)) {
                updateCurrentUser();
            }
        }
    }

    public static List<User> queryUsers() {
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        return ud.queryBuilder().list();
    }

    public static void deleteGroupList(String uid, boolean isDeleteSystemGroup) {
        QueryBuilder<GroupList> qb = GreenDaoManager
                .getDaoSession()
                .getGroupListDao()
                .queryBuilder();

        if (isDeleteSystemGroup) {
            qb.where(GroupListDao.Properties.Uid.eq(uid));
        } else {
            qb.where(GroupListDao.Properties.Uid.eq(uid)
                    , GroupListDao.Properties.Visible.notEq("-1"));
        }

        qb.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static void deleteGroupList(GroupList groupList) {
        QueryBuilder<GroupList> qb = GreenDaoManager
                .getDaoSession()
                .getGroupListDao()
                .queryBuilder()
                .where(GroupListDao.Properties.Gid.eq(groupList.getGid()))
                .where(GroupListDao.Properties.Uid.eq(groupList.getUid()));

        qb.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static void insertGroupList(GroupList groupList) {
        GreenDaoManager
                .getDaoSession()
                .getGroupListDao()
                .insert(groupList);
    }

    public static void insertGroupList(List<GroupList> groupLists) {
        GreenDaoManager
                .getDaoSession()
                .getGroupListDao()
                .insertInTx(groupLists);
    }

    public static void updateGroupList(GroupList groupList) {
        GroupListDao groupListDao = GreenDaoManager
                .getDaoSession()
                .getGroupListDao();
        List<GroupList> gls = groupListDao
                .queryBuilder()
                .where(GroupListDao.Properties.Gid.eq(groupList.getGid()))
                .where(GroupListDao.Properties.Uid.eq(groupList.getUid()))
                .list();
        if(!CommonUtil.isEmpty(gls)){
            groupList.setId(gls.get(0).getId());
            groupListDao.update(groupList);
        }
    }

    public static void updateGroupList(List<GroupList> groupLists) {
        updateGroupList(groupLists, UserUtil.getUid(), true);
    }

    public static void updateGroupList(List<GroupList> groupLists, boolean isUpdateSystemGroup) {
        updateGroupList(groupLists, UserUtil.getUid(), isUpdateSystemGroup);
    }

    public static void updateGroupList(List<GroupList> groupLists, String uid, boolean isUpdateSystemGroup) {
        if (CommonUtil.isEmpty(groupLists)) {
            return;
        }

        deleteGroupList(uid, isUpdateSystemGroup);

        groupLists = GroupList.orderGroupList(groupLists);
        GroupList gl = groupLists.get(0);
        if (!isUpdateSystemGroup && gl.getVisible() == -1) {
            groupLists.remove(0);
        }
        insertGroupList(groupLists);
    }

    public static List<GroupList> queryGroupList() {
        return queryGroupList(true);
    }

    public static List<GroupList> queryGroupList(boolean isQuerySystemGroup) {
        if (isUserEmpty()) {
            return null;
        }
        return queryGroupList(getCurrentUser().getUid(), isQuerySystemGroup);
    }


    public static List<GroupList> queryGroupList(String uid, boolean isQuerySystemGroup) {

        QueryBuilder<GroupList> qb = GreenDaoManager
                .getDaoSession()
                .getGroupListDao()
                .queryBuilder();

        if (isQuerySystemGroup) {
            qb.where(GroupListDao.Properties.Uid.eq(uid));
        } else {
            qb.where(GroupListDao.Properties.Uid.eq(uid)
                    , GroupListDao.Properties.Visible.notEq("-1"));
        }

        try {
            return qb.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
