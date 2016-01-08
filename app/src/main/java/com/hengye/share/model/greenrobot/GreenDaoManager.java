package com.hengye.share.model.greenrobot;

import com.hengye.share.BaseApplication;

public class GreenDaoManager {

    public final static String DB_NAME = "ShareDao";

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    /**
     * 取得DaoMaster
     *
     * @return
     */
    public static DaoMaster getDaoMaster()
    {
        if (daoMaster == null)
        {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(BaseApplication.getInstance(), DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }
    /**
     * 取得DaoSession
     *
     * @return
     */
    public static DaoSession getDaoSession()
    {
        if (daoSession == null)
        {
            if (daoMaster == null)
            {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
