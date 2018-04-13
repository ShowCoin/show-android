package mobi.hifun.seeu.home.mode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import mobi.hifun.seeu.api.BaseAPI;
import mobi.hifun.seeu.api.BaseBizRequest;
import mobi.hifun.seeu.po.POAddress;
import tv.beke.base.po.POCommonResp;
import tv.beke.base.po.POMember;

/**
 * 获取地理位置信息 16/5/28.
 */
public abstract class AddressRequest extends BaseBizRequest<POAddress> {

    @Override
    public String getPath() {
        return BaseAPI.Path.getProvince;
    }

    @Override
    public void onRequestResult(String result) {

        Type type = new TypeToken<POCommonResp<POAddress>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }
    public void getData(Map<String,String> map){
        startRequest(map);
    }

}
