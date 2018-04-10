package one.show.live.live.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.po.POCommonResp;
import one.show.live.live.po.POInitLive;

public abstract class StartLiveRequest extends BaseBizRequest<POInitLive>{
    @Override
    public String getPath() {
        return BaseAPI.Path.initLive;
    }

    @Override
    public void onRequestResult(String result) {
        Type type = new TypeToken<POCommonResp<POInitLive>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }

}
