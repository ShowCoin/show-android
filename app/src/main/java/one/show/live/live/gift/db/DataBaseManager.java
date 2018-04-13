package one.show.live.live.gift.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import one.show.live.live.po.POGift;

public class DataBaseManager {

    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DataBaseManager(Context context) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 插入数据
     *
     * @param giftsBean
     */
    public void add(List<POGift> giftsBean) {
        if (!db.isOpen()) {
            return;
        }

        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try {
            for (POGift gift : giftsBean) {
                db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_NAME
                        + " VALUES(?,?,?,?,?,?,?)", new
                        Object[]{gift.getId(), gift.getExp(), gift.getName(), gift
                        .getIcon(), gift.getImage(), gift.getPrice(), gift
                        .getType()});
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        } finally {
            db.endTransaction(); // 结束事务
        }
    }

    /**
     * 更新数据
     *
     * @param giftsBean
     */
    public void updateAge(POGift giftsBean) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.GiftId, giftsBean.getId());
        db.update(DatabaseHelper.TABLE_NAME, cv, "giftid = ?",
                new String[]{giftsBean.getName()});
    }

    /**
     * 查询数据
     *
     * @return
     */
    public List<POGift> query() {
        ArrayList<POGift> gifts = new ArrayList<POGift>();
        Cursor c = queryTheCursor();
        while (c.moveToNext()) {
            POGift giftsBean = new POGift();
            giftsBean.setId(c.getInt(c.getColumnIndex(DatabaseHelper.GiftId)));
            giftsBean.setExp(c.getInt(c.getColumnIndex(DatabaseHelper.GiftExp)));
            giftsBean.setName(c.getString(c.getColumnIndex(DatabaseHelper.GiftName)));
            giftsBean.setIcon(c.getString(c.getColumnIndex(DatabaseHelper.GiftIcon)));
            giftsBean.setImage(c.getString(c.getColumnIndex(DatabaseHelper.GiftImage)));
            giftsBean.setPrice(c.getInt(c.getColumnIndex(DatabaseHelper.GiftPrice)));
            giftsBean.setType(c.getInt(c.getColumnIndex(DatabaseHelper.GiftType)));
            gifts.add(giftsBean);
        }
        c.close();
        return gifts;
    }

    /**
     * 查询礼物
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME,
                null);
        return c;
    }

    /**
     * 删除表
     *
     * @param TableName
     */
    public void deleteTableData(String TableName) {
        if (!db.isOpen()) {
            return;
        }
        db.delete(TableName, null, null);
    }

    /**
     * close database
     */
    public void closeDB() {
        // 释放数据库资源
        db.close();
    }
}
