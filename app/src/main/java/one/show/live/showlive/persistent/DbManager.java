package one.show.live.showlive.persistent;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.util.HashMap;

import one.show.live.showlive.BuildConfig;

/**
 * Simply database module, use with {@link DbOpenHelper} for DAO creation.
 */

public enum DbManager {
    INSTANCE;
    private final static String DEFAULT_DB_NAME = "default.db";

    private Class[] tableClasses;

    private Context ctx;

    private DbOpenHelper dbOpenHelper;

    private String dbName = DEFAULT_DB_NAME;

    private HashMap<String, Dao> daoCache;

    private int dbVersion = BuildConfig.VERSION_CODE;

    boolean isInit = false;

    DbManager() {
    }


    /**
     * @param ctx
     * @param tableClasses Array of table classes.
     */
    public void init(Context ctx, Class[] tableClasses) {
        this.ctx = ctx;
        this.tableClasses = tableClasses;
        this.daoCache = new HashMap<>();
        isInit = true;
    }


    public boolean isInit() {
        return this.isInit;
    }

    /**
     * Obtain a dao instance by class of data model.
     *
     * @param tableClazz
     * @param <T>
     * @return
     */
    public <T> Dao<T, Object> getDao(Class<T> tableClazz) {

        if (null == dbOpenHelper) {
            dbOpenHelper = new DbOpenHelper(ctx
                    , dbName
                    , dbVersion
                    , tableClasses);
        }

        try {
            if (daoCache.containsKey(tableClazz.getName())) {
                return daoCache.get(tableClazz.getName());
            } else {
                Dao<T, Object> dao = dbOpenHelper
                        .getDao(tableClazz);
                daoCache.put(tableClazz.getName(), dao);
                return dao;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
