package com.hengye.share.util;

import com.hengye.share.model.Parent;
import com.hengye.share.model.greenrobot.GreenDaoManager;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.greenrobot.GroupListDao;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.greenrobot.UserDao;
import com.hengye.share.model.sina.WBUserInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

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

    public static void changeCurrentUser(User user){
        SPUtil.setUid(user.getUid());
        mCurrentUser = user;
    }

    public static void deleteUser(User user){
        if(mCurrentUser != null && mCurrentUser.equals(user)){
            mCurrentUser = null;
            SPUtil.setUid(null);
        }
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        ud.delete(user);
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
            List<User> users = ud
                    .queryBuilder()
                    .where(UserDao.Properties.Uid.eq(uid))
                    .list();
//            List<User> users = ud.queryRaw("where UID = ?", uid);
            if (!CommonUtil.isEmptyCollection(users)) {
                user = users.get(0);
            }
        }

        if (user == null) {
            UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
            List<User> users = ud
                    .queryBuilder()
                    .limit(1)
                    .list();
//           List<User> users = ud.queryRaw("limit 1");
            if (!CommonUtil.isEmptyCollection(users)) {
                user = users.get(0);
            }
        }
        return user;
    }

    public static void updateUser(Oauth2AccessToken accessToken) {
        User user;
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        List<User> users = ud
                .queryBuilder()
                .where(UserDao.Properties.Uid.eq(accessToken.getUid())
                        , UserDao.Properties.ParentType.eq(Parent.TYPE_WEIBO))
                .list();
//        List<User> users = ud.queryRaw("where UID = ? and PARENT_TYPE = ?", accessToken.getUid(), Parent.TYPE_WEIBO + "");
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
        List<User> users = ud
                .queryBuilder()
                .where(UserDao.Properties.Uid.eq(wbUserInfo.getIdstr())
                        , UserDao.Properties.ParentType.eq(Parent.TYPE_WEIBO))
                .list();
//        List<User> users = ud.queryRaw("where UID = ? and PARENT_TYPE = ?", wbUserInfo.getIdstr(), Parent.TYPE_WEIBO + "");
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

    public static void updateUser(User targetUser) {
        User user;
        UserDao ud = GreenDaoManager.getDaoSession().getUserDao();
        List<User> users = ud.queryBuilder().where(UserDao.Properties.Uid.eq(targetUser.getUid())).list();
//        List<User> users = ud.queryRaw("where UID = ?", targetUser.getUid());
        if (!CommonUtil.isEmptyCollection(users)) {
            user = users.get(0);
            targetUser.setId(user.getId());
            ud.update(targetUser);
        }
    }

    public static List<User> queryUsers(){
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

    public static void insertGroupList(List<GroupList> groupLists) {
        GreenDaoManager
                .getDaoSession()
                .getGroupListDao()
                .insertInTx(groupLists);
    }

    public static void updateGroupList(List<GroupList> groupLists) {
        updateGroupList(groupLists, UserUtil.getUid(), true);
    }

    public static void updateGroupList(List<GroupList> groupLists, boolean isUpdateSystemGroup) {
        updateGroupList(groupLists, UserUtil.getUid(), isUpdateSystemGroup);
    }

    public static void updateGroupList(List<GroupList> groupLists, String uid, boolean isUpdateSystemGroup) {
        if (CommonUtil.isEmptyCollection(groupLists)) {
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
        User user = getCurrentUser();
        if (user == null) {
            return null;
        }
        return queryGroupList(user.getUid(), isQuerySystemGroup);
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

        return qb
                .where(GroupListDao.Properties.Uid.eq(uid))
                .list();
    }
}
