package one.show.live.home.mode;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;


/**
 * 获取地理位置信息
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
