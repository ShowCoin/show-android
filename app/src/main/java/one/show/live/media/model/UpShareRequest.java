package one.show.live.media.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.common.po.POCommonResp;

public abstract class UpShareRequest extends BaseBizRequest<Object> {
    /**
     * 固定服务器的path地址
     */
    @Override
    public String getPath() {
        return BaseAPI.Path.share;
    }

    /**
     * JSON解析
     *
     * @param result
     */
    @Override
    public void onRequestResult(String result) {
        Type type = new TypeToken<POCommonResp<Object>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }

    public void setData(String name, String vid) {
        Map<String, String> map = new HashMap<>();
        map.put("channel", name);
        map.put("vid", vid);
        startRequest(map);
    }
}
