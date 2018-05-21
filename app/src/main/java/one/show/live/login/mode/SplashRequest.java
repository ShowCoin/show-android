package one.show.live.login.mode;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.common.po.POCommonResp;
import one.show.live.log.Logger;
import one.show.live.po.POConfig;

/**
 * Created by apple on 16/6/3.
 */
public abstract class SplashRequest extends BaseBizRequest<POConfig> {
    /**
     * 固定服务器的path地址
     */
    @Override
    public String getPath() {
        return BaseAPI.Path.config;
    }

    /**
     * JSON解析
     *
     * @param result
     */
    @Override
    public void onRequestResult(String result) {
<<<<<<< HEAD
        Logger.d("liuzehua",result);
=======
        Logger.d("Nano",result);
>>>>>>> master
        Type type = new TypeToken<POCommonResp<POConfig>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }
}
