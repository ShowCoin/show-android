package one.show.live.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigCacheManager {
    private static ConfigCacheManager configDefaults;
    private SharedPreferences sharedPreferences;

    private ConfigCacheManager() {

    }

    /**
     * 初始化
     * @param context Context
     */
    public static void init(Context context) {
        configDefaults = new ConfigCacheManager();
        configDefaults.sharedPreferences = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
    }

    /**
     * 获取单例
     * @return UserDefaults
     */
    public static ConfigCacheManager getInstance() {
        return configDefaults;
    }

    /**
     * 保存数据
     */
    public void setValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
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
