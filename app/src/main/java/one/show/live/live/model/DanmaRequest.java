package one.show.live.live.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.api.BasePayRequest;
import one.show.live.api.CommonRequest;
import one.show.live.po.POCommonResp;

public class DanmaRequest extends CommonRequest<Object> {
    @Override
    public String getRequestUrl() {
        return "http://pay.beke.tv"+BaseAPI.Path.danma;
    }

    @Override
    public void onRequestResult(String result) {
        System.out.println(result);
        Type type = new TypeToken<POCommonResp<Object>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }

    @Override
    public void onFinish(boolean isSuccess, String msg, Object data) {

    }

}
