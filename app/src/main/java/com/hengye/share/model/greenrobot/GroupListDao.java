package com.hengye.share.model.greenrobot;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.hengye.share.model.greenrobot.GroupList;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GROUP_LIST".
*/
public class GroupListDao extends AbstractDao<GroupList, Long> {

    public static final String TABLENAME = "GROUP_LIST";

    /**
     * Properties of entity GroupList.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property InsertNumber = new Property(1, int.class, "insertNumber", false, "INSERT_NUMBER");
        public final static Property Uid = new Property(2, String.class, "uid", false, "UID");
        public final static Property Gid = new Property(3, String.class, "gid", false, "GID");
        public final static Property Title = new Property(4, String.class, "title", false, "TITLE");
        public final static Property Name = new Property(5, String.class, "name", false, "NAME");
        public final static Property Count = new Property(6, Integer.class, "count", false, "COUNT");
        public final static Property Type = new Property(7, Integer.class, "type", false, "TYPE");
        public final static Property Visible = new Property(8, Integer.class, "visible", false, "VISIBLE");
        public final static Property Remind = new Property(9, Integer.class, "remind", false, "REMIND");
    };

    private DaoSession daoSession;


    public GroupListDao(DaoConfig config) {
        super(config);
    }
    
    public GroupListDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GROUP_LIST\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ASC AUTOINCREMENT ," + // 0: id
                "\"INSERT_NUMBER\" INTEGER NOT NULL ," + // 1: insertNumber
                "\"UID\" TEXT NOT NULL ," + // 2: uid
                "\"GID\" TEXT NOT NULL ," + // 3: gid
                "\"TITLE\" TEXT NOT NULL ," + // 4: title
                "\"NAME\" TEXT NOT NULL ," + // 5: name
                "\"COUNT\" INTEGER," + // 6: count
                "\"TYPE\" INTEGER," + // 7: type
                "\"VISIBLE\" INTEGER," + // 8: visible
                "\"REMIND\" INTEGER);"); // 9: remind
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GROUP_LIST\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, GroupList entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getInsertNumber());
        stmt.bindString(3, entity.getUid());
        stmt.bindString(4, entity.getGid());
        stmt.bindString(5, entity.getTitle());
        stmt.bindString(6, entity.getName());
 
        Integer count = entity.getCount();
        if (count != null) {
            stmt.bindLong(7, count);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(8, type);
        }
 
        Integer visible = entity.getVisible();
        if (visible != null) {
            stmt.bindLong(9, visible);
        }
 
        Integer remind = entity.getRemind();
        if (remind != null) {
            stmt.bindLong(10, remind);
        }
    }

    @Override
    protected void attachEntity(GroupList entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public GroupList readEntity(Cursor cursor, int offset) {
        GroupList entity = new GroupList( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getInt(offset + 1), // insertNumber
            cursor.getString(offset + 2), // uid
            cursor.getString(offset + 3), // gid
            cursor.getString(offset + 4), // title
            cursor.getString(offset + 5), // name
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // count
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // type
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // visible
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9) // remind
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, GroupList entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setInsertNumber(cursor.getInt(offset + 1));
        entity.setUid(cursor.getString(offset + 2));
        entity.setGid(cursor.getString(offset + 3));
        entity.setTitle(cursor.getString(offset + 4));
        entity.setName(cursor.getString(offset + 5));
        entity.setCount(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setType(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setVisible(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setRemind(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(GroupList entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(GroupList entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}