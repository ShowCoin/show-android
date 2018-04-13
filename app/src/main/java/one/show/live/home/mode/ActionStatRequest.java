package mobi.hifun.seeu.home.mode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import mobi.hifun.seeu.api.BaseAPI;
import mobi.hifun.seeu.api.BaseBizRequest;
import mobi.hifun.seeu.po.POAddress;
import tv.beke.base.po.POCommonResp;

/**
 * 获取地理位置信息 16/5/28.
 */
public abstract class ActionStatRequest extends BaseBizRequest<Object> {

    @Override
    public String getPath() {
        return BaseAPI.Path.stat_action;
    }

    @Override
    public void onRequestResult(String result) {

        Type type = new TypeToken<POCommonResp<Object>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }
    public void getData(Map<String,String> map){
        startRequest(map);
    }

}
