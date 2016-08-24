package com.hengye.share.model.greenrobot.migrator;

import android.database.sqlite.SQLiteDatabase;

import com.hengye.share.model.greenrobot.TopicDraftDao;

/**
 * Created by yuhy on 16/8/23.
 */
public class DBMigrationHelper1005 extends AbstractMigratorHelper {

/* Upgrade from DB schema x to schema x+1 */

    public void onUpgrade(SQLiteDatabase db) {
        TopicDraftDao.dropTable(db, true);
        TopicDraftDao.createTable(db, false);
//        db.execSQL("ALTER TABLE TopicDraft ADD COLUMN TARGET_COMMENT_CONTENT TEXT");
    }
}
