package one.show.live.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import one.show.live.common.db.helper.MigrationHelper;
import one.show.live.common.greendao.DaoMaster;
import one.show.live.common.greendao.POLoginDao;

/**
 * Created by ydeng on 2018/1/1.
 */

public class MyOpenHelper extends DaoMaster.OpenHelper {

    private static final String TAG = "MyOpenHelper";

    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        super.onUpgrade(db, oldVersion, newVersion);
        Log.e(TAG, "数据库升级");
        MigrationHelper.getInstance().migrate(db, POLoginDao.class);
    }
}
