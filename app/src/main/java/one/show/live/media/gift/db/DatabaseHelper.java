package one.show.live.media.gift.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String GiftId = "id";
    public static String GiftExp = "exp";
    public static String GiftName = "name";
    public static String GiftIcon = "icon";
    public static String GiftImage = "image";
    public static String GiftPrice = "price";
    public static String GiftType = "type";



    // 数据库版本号
    private static final int DATABASE_VERSION = 1;
    // 数据库名
    private static final String DATABASE_NAME = "gift.db";

    // 数据表名
    public static final String TABLE_NAME = "gift";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createtable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createtable(SQLiteDatabase db) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
        stringBuilder.append(TABLE_NAME);
        stringBuilder.append("(" + DatabaseHelper.GiftId).append(" INTEGER, ");
        stringBuilder.append(DatabaseHelper.GiftExp).append(" INTEGER, ");
        stringBuilder.append(DatabaseHelper.GiftName).append(" TEXT, ");
        stringBuilder.append(DatabaseHelper.GiftIcon).append(" TEXT, ");
        stringBuilder.append(DatabaseHelper.GiftImage).append(" TEXT, ");
        stringBuilder.append(DatabaseHelper.GiftPrice).append(" INTEGER, ");
        stringBuilder.append(DatabaseHelper.GiftType).append(" INTEGER ); ");
        db.execSQL(stringBuilder.toString());
    }
}
