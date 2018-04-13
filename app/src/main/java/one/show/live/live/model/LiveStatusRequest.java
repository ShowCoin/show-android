package one.show.live.live.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.po.POCommonResp;
import one.show.live.live.po.POInitLive;
import one.show.live.live.po.POLiveStatus;

public abstract class LiveStatusRequest extends BaseBizRequest<POLiveStatus>{
    @Override
    public String getPath() {
        return BaseAPI.Path.liveStatus;
    }

    @Override
    public void onRequestResult(String result) {
        System.out.println(result);
        Type type = new TypeToken<POCommonResp<POLiveStatus>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }

}
