package com.hengye.share.model.greenrobot;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hengye.share.model.greenrobot.migrator.AbstractMigratorHelper;
import com.hengye.share.ui.base.BaseApplication;

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
            DaoMaster.OpenHelper helper = new UpgradeHelper(BaseApplication.getInstance(), DB_NAME, null);
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

    public static class UpgradeHelper extends DaoMaster.OpenHelper {

        private final static String DB_MIGRATOR_PACKAGE_NAME = AbstractMigratorHelper.class.getPackage().getName();

        public UpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        /**
         * Here is where the calls to upgrade are executed
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /* i represent the version where the user is now and the class named with this number implies that is upgrading from i to i++ schema */
            for (int i = oldVersion; i < newVersion; i++) {
                try {
                /* New instance of the class that migrates from i version to i++ version named DBMigratorHelper{version that the db has on this moment} */
                    AbstractMigratorHelper migratorHelper = (AbstractMigratorHelper) Class.forName(DB_MIGRATOR_PACKAGE_NAME + i).newInstance();

                    if (migratorHelper != null) {

                        Log.e("GreenDao", "Migrate from schema from schema: " + i + " to " + i++);

                    /* Upgrade de db */
                        migratorHelper.onUpgrade(db);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("GreenDao", "Could not migrate from schema from schema: " + i + " to " + i++);
                /* If something fail prevent the DB to be updated to future version if the previous version has not been upgraded successfully */
                    break;
                }


            }
        }
    }



}
