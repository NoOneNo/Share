package com.hengye.share.model.greenrobot.migrator;

import org.greenrobot.greendao.database.Database;

/**
 * Created by yuhy on 16/8/23.
 */
public abstract class AbstractMigratorHelper {

    public abstract void onUpgrade(Database db);

}
