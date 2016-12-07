package com.hengye.share.model.greenrobot.migrator;


import org.greenrobot.greendao.database.Database;

/**
 * Created by yuhy on 16/8/23.
 */
public class DBMigrationHelper3 extends AbstractMigratorHelper {

/* Upgrade from DB schema x to schema x+1 */

    /**
     * 增加cookie和extra字段
     * @param db
     */
    public void onUpgrade(Database db) {
        db.execSQL("ALTER TABLE GROUP_LIST ADD COLUMN DESCRIPTION TEXT");
    }
}