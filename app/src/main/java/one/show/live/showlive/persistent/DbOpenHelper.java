package one.show.live.showlive.persistent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Extends from {@link OrmLiteSqliteOpenHelper} which help to create or upgrade database.
 */

public class DbOpenHelper
        extends OrmLiteSqliteOpenHelper {

    private Class[] tableClasses;

    public DbOpenHelper(Context context
            , String databaseName
            , int databaseVersion
            , Class[] tableClasses) {

        super(context
                , databaseName
                , null, databaseVersion);
        this.tableClasses = tableClasses;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase
            , ConnectionSource connectionSource) {

        try {
            for (Class clazz : tableClasses) {
                TableUtils
                        .createTable(connectionSource, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase
            , ConnectionSource connectionSource, int i, int i1) {
        try {
            for (Class clazz : tableClasses) {
                TableUtils
                        .dropTable(connectionSource, clazz, true);
                TableUtils
                        .createTable(connectionSource, clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
