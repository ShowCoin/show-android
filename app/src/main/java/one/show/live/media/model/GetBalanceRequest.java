package one.show.live.media.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.api.BasePayRequest;
import one.show.live.common.api.CommonRequest;
import one.show.live.common.po.POCommonResp;
import one.show.live.po.POBalance;

/**
 * Created by apple on 16/6/11.
 */
public abstract class GetBalanceRequest extends CommonRequest<POBalance> {

    @Override
    public String getRequestUrl() {
        return "http://pay.beke.tv"+BaseAPI.Path.balance;
    }


    @Override
    public void onRequestResult(String result) {

        Type type = new TypeToken<POCommonResp<POBalance>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }
}
