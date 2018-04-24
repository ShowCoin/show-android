package one.show.live.media.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.common.po.POCommonResp;
import one.show.live.media.po.POLiveStatus;


public abstract class LiveStatusRequest extends BaseBizRequest<POLiveStatus> {
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
