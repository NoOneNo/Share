package com.hengye.share.model.greenrobot.migrator;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yuhy on 16/8/23.
 */
public class DBMigrationHelper1005 extends AbstractMigratorHelper {

/* Upgrade from DB schema x to schema x+1 */

    public void onUpgrade(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE user ADD COLUMN USERNAME TEXT");
    }
}
