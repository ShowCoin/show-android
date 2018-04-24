package one.show.live.media.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.common.po.POCommonResp;

public abstract class KickRequest extends BaseBizRequest<Object>{
    @Override
    public String getPath() {
        return BaseAPI.Path.kick;
    }

    @Override
    public void onRequestResult(String result) {
        System.out.println(result);
        Type type = new TypeToken<POCommonResp<Object>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }

}
