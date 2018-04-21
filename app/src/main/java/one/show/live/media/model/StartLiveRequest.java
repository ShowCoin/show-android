package one.show.live.media.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.common.po.POCommonResp;
import one.show.live.media.po.POInitLive;


public abstract class StartLiveRequest extends BaseBizRequest<POInitLive> {
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
