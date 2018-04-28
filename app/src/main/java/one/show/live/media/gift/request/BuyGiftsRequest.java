package one.show.live.media.gift.request;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import one.show.live.api.BaseAPI;
import one.show.live.api.BasePayRequest;
import one.show.live.common.api.CommonRequest;
import one.show.live.common.po.POCommonResp;


/**
 * Created by genghuayun on ..3/7.
 */
public abstract class BuyGiftsRequest extends CommonRequest<Object> {
    @Override
    public String getRequestUrl() {
        return "http://pay.show.tv"+BaseAPI.Path.buyGift;
    }


    @Override
    public void onRequestResult(String result) {
        Type type = new TypeToken<POCommonResp<Object>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }
}
