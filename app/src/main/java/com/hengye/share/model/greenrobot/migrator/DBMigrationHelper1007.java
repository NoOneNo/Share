package com.hengye.share.model.greenrobot.migrator;

import com.hengye.share.model.greenrobot.TopicDraftDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by yuhy on 16/8/23.
 */
public class DBMigrationHelper1007 extends AbstractMigratorHelper {

/* Upgrade from DB schema x to schema x+1 */

    public void onUpgrade(Database db) {
        TopicDraftDao.dropTable(db, true);
        TopicDraftDao.createTable(db, false);
//        db.execSQL("ALTER TABLE TopicDraft ADD COLUMN TARGET_COMMENT_CONTENT TEXT");
    }
}
