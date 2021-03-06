package com.hengye.share.model.greenrobot;

import org.greenrobot.greendao.annotation.*;

import org.greenrobot.greendao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import android.support.annotation.NonNull;

import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;
import com.hengye.share.util.thirdparty.WBUtil;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
// KEEP INCLUDES END

/**
 * Entity mapped to table "SHARE_JSON".
 */
@Entity(active = true)
public class ShareJson implements java.io.Serializable {

    @Id
    @NotNull
    private String model;
    private String json;

    /** Used to resolve relations */
    @Generated
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated
    private transient ShareJsonDao myDao;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    public ShareJson() {
    }

    public ShareJson(String model) {
        this.model = model;
    }

    @Generated
    public ShareJson(String model, String json) {
        this.model = model;
        this.json = json;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getShareJsonDao() : null;
    }

    @NotNull
    public String getModel() {
        return model;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setModel(@NotNull String model) {
        this.model = model;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void delete() {
        __throwIfDetached();
        myDao.delete(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void update() {
        __throwIfDetached();
        myDao.update(this);
    }

    /**
    * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
    * Entity must attached to an entity context.
    */
    @Generated
    public void refresh() {
        __throwIfDetached();
        myDao.refresh(this);
    }

    @Generated
    private void __throwIfDetached() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
    }

    // KEEP METHODS - put your custom methods here

    public static void saveListData(final String modelName, List data) {
        saveListData(modelName, data, true);
    }

    public static void saveListData(final String modelName, List data, boolean limitData) {
        if (data == null) {
            data = Collections.emptyList();
        }
        Single
                .just(data)
                .flatMap(new Function<List, SingleSource<List>>() {
                    @Override
                    public SingleSource<List> apply(@NonNull List data) {
                        int requestCount = WBUtil.getWBStatusRequestCount();
                        int maxCacheCount = requestCount * 2;
                        if (requestCount > 0 && !data.isEmpty() && data.size() > maxCacheCount) {
                            data = data.subList(0, maxCacheCount);
                        }
                        return Single.just(data);
                    }
                })
                .doOnSuccess(new Consumer<List>() {
                    @Override
                    public void accept(List data) throws Exception {
                        GreenDaoManager
                                .getDaoSession()
                                .getShareJsonDao()
                                .insertOrReplace(new ShareJson(modelName, GsonUtil.toJson(data)));
                    }
                })
                .observeOn(SchedulerProvider.io())
                .subscribe();
    }

    public static <T> T findData(String modelName, Type type) {
        ShareJson shareJson = null;

        try {
            shareJson = GreenDaoManager.getDaoSession().getShareJsonDao().load(modelName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (shareJson == null) {
            return null;
        }

        return GsonUtil.fromJson(shareJson.getJson(), type);

    }
    // KEEP METHODS END

}
