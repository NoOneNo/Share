package com.hengye.share.model.greenrobot.migrator;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yuhy on 16/8/23.
 */
public abstract class AbstractMigratorHelper {

    public abstract void onUpgrade(SQLiteDatabase db);

}
