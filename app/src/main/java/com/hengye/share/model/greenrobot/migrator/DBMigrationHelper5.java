package com.hengye.share.model.greenrobot.migrator;


import com.hengye.share.model.greenrobot.StatusDraftDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by yuhy on 16/8/23.
 */
public class DBMigrationHelper5 extends AbstractMigratorHelper {

/* Upgrade from DB schema x to schema x+1 */

    /**
     * 增加cookie和extra字段
     * @param db
     */
    public void onUpgrade(Database db) {
        StatusDraftDao.createTable(db, true);
//        StatusDraftDao.dropTable(db, true);
    }
}