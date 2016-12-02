package com.hengye.share.model.greenrobot;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Uid = new Property(1, String.class, "uid", false, "UID");
        public final static Property Token = new Property(2, String.class, "token", false, "TOKEN");
        public final static Property ParentType = new Property(3, int.class, "parentType", false, "PARENT_TYPE");
        public final static Property ParentJson = new Property(4, String.class, "parentJson", false, "PARENT_JSON");
        public final static Property RefreshToken = new Property(5, String.class, "refreshToken", false, "REFRESH_TOKEN");
        public final static Property ExpiresIn = new Property(6, Long.class, "expiresIn", false, "EXPIRES_IN");
        public final static Property Name = new Property(7, String.class, "name", false, "NAME");
        public final static Property Avatar = new Property(8, String.class, "avatar", false, "AVATAR");
        public final static Property Gender = new Property(9, String.class, "gender", false, "GENDER");
        public final static Property Sign = new Property(10, String.class, "sign", false, "SIGN");
        public final static Property Cover = new Property(11, String.class, "cover", false, "COVER");
        public final static Property Account = new Property(12, String.class, "account", false, "ACCOUNT");
        public final static Property Password = new Property(13, String.class, "password", false, "PASSWORD");
        public final static Property AdToken = new Property(14, String.class, "adToken", false, "AD_TOKEN");
        public final static Property Cookie = new Property(15, String.class, "cookie", false, "COOKIE");
        public final static Property Extra = new Property(16, String.class, "extra", false, "EXTRA");
    }

    private DaoSession daoSession;


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ASC AUTOINCREMENT ," + // 0: id
                "\"UID\" TEXT NOT NULL ," + // 1: uid
                "\"TOKEN\" TEXT NOT NULL ," + // 2: token
                "\"PARENT_TYPE\" INTEGER NOT NULL ," + // 3: parentType
                "\"PARENT_JSON\" TEXT," + // 4: parentJson
                "\"REFRESH_TOKEN\" TEXT," + // 5: refreshToken
                "\"EXPIRES_IN\" INTEGER," + // 6: expiresIn
                "\"NAME\" TEXT," + // 7: name
                "\"AVATAR\" TEXT," + // 8: avatar
                "\"GENDER\" TEXT," + // 9: gender
                "\"SIGN\" TEXT," + // 10: sign
                "\"COVER\" TEXT," + // 11: cover
                "\"ACCOUNT\" TEXT," + // 12: account
                "\"PASSWORD\" TEXT," + // 13: password
                "\"AD_TOKEN\" TEXT," + // 14: adToken
                "\"COOKIE\" TEXT," + // 15: cookie
                "\"EXTRA\" TEXT);"); // 16: extra
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUid());
        stmt.bindString(3, entity.getToken());
        stmt.bindLong(4, entity.getParentType());
 
        String parentJson = entity.getParentJson();
        if (parentJson != null) {
            stmt.bindString(5, parentJson);
        }
 
        String refreshToken = entity.getRefreshToken();
        if (refreshToken != null) {
            stmt.bindString(6, refreshToken);
        }
 
        Long expiresIn = entity.getExpiresIn();
        if (expiresIn != null) {
            stmt.bindLong(7, expiresIn);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(8, name);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(9, avatar);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(10, gender);
        }
 
        String sign = entity.getSign();
        if (sign != null) {
            stmt.bindString(11, sign);
        }
 
        String cover = entity.getCover();
        if (cover != null) {
            stmt.bindString(12, cover);
        }
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(13, account);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(14, password);
        }
 
        String adToken = entity.getAdToken();
        if (adToken != null) {
            stmt.bindString(15, adToken);
        }
 
        String cookie = entity.getCookie();
        if (cookie != null) {
            stmt.bindString(16, cookie);
        }
 
        String extra = entity.getExtra();
        if (extra != null) {
            stmt.bindString(17, extra);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUid());
        stmt.bindString(3, entity.getToken());
        stmt.bindLong(4, entity.getParentType());
 
        String parentJson = entity.getParentJson();
        if (parentJson != null) {
            stmt.bindString(5, parentJson);
        }
 
        String refreshToken = entity.getRefreshToken();
        if (refreshToken != null) {
            stmt.bindString(6, refreshToken);
        }
 
        Long expiresIn = entity.getExpiresIn();
        if (expiresIn != null) {
            stmt.bindLong(7, expiresIn);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(8, name);
        }
 
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(9, avatar);
        }
 
        String gender = entity.getGender();
        if (gender != null) {
            stmt.bindString(10, gender);
        }
 
        String sign = entity.getSign();
        if (sign != null) {
            stmt.bindString(11, sign);
        }
 
        String cover = entity.getCover();
        if (cover != null) {
            stmt.bindString(12, cover);
        }
 
        String account = entity.getAccount();
        if (account != null) {
            stmt.bindString(13, account);
        }
 
        String password = entity.getPassword();
        if (password != null) {
            stmt.bindString(14, password);
        }
 
        String adToken = entity.getAdToken();
        if (adToken != null) {
            stmt.bindString(15, adToken);
        }
 
        String cookie = entity.getCookie();
        if (cookie != null) {
            stmt.bindString(16, cookie);
        }
 
        String extra = entity.getExtra();
        if (extra != null) {
            stmt.bindString(17, extra);
        }
    }

    @Override
    protected final void attachEntity(User entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // uid
            cursor.getString(offset + 2), // token
            cursor.getInt(offset + 3), // parentType
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // parentJson
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // refreshToken
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // expiresIn
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // name
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // avatar
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // gender
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // sign
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // cover
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // account
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // password
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // adToken
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // cookie
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16) // extra
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUid(cursor.getString(offset + 1));
        entity.setToken(cursor.getString(offset + 2));
        entity.setParentType(cursor.getInt(offset + 3));
        entity.setParentJson(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setRefreshToken(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setExpiresIn(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAvatar(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setGender(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setSign(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCover(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setAccount(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setPassword(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setAdToken(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setCookie(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setExtra(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(User entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
