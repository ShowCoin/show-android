package one.show.live.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PublicCacheManager {

    private static PublicCacheManager publicDefaults;
    private SharedPreferences sharedPreferences;

    private PublicCacheManager() {

    }

    /**
     * 初始化
     * @param context Context
     */
    public static void init(Context context) {
        publicDefaults = new PublicCacheManager();
        publicDefaults.sharedPreferences = context.getSharedPreferences("PUBLIC", Context.MODE_PRIVATE);
    }

    /**
     * 获取单例
     * @return UserDefaults
     */
    public static PublicCacheManager getInstance() {
        return publicDefaults;
    }

    /**
     * 保存数据
     */
    public void setValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setLongValue(String key, long value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLongValue(String key, long defValue){
        return sharedPreferences.getLong(key, defValue);
    }


    /**
     * 获取数据
     */
    public String getValue(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * 删除
     */
    public void remove(String key){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
