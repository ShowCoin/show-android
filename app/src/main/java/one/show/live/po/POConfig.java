package one.show.live.po;

import com.google.gson.Gson;

import java.io.Serializable;

import one.show.live.util.ConfigCacheManager;

/**
 * Created by liuzehua on 2018/4/11.
 */

public class POConfig implements Serializable{

    private boolean isOk;//判断poconfig里面是不是有数据
    private static POConfig mInstanceConfig;

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    /**
     * 保存数据
     *
     * @param mpoConfig
     */
    public static void save(POConfig mpoConfig) {
        mpoConfig.setOk(true);
        ConfigCacheManager.getInstance().setValue("Config", new Gson().toJson(mpoConfig));
        mInstanceConfig = mpoConfig;
    }

    /**
     * 获取配置信息
     */
    public static POConfig getInstance() {
        if (mInstanceConfig == null) {
            synchronized (POConfig.class) {
                if (mInstanceConfig == null) {
                    mInstanceConfig = new Gson().fromJson(ConfigCacheManager.getInstance().getValue("Config", "{}"), POConfig.class);
                }
            }
        }

        if (mInstanceConfig == null) {
            return new POConfig();
        }
        return mInstanceConfig;
    }

}
