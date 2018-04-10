package one.show.live.live.gift.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.po.POCommonResp;
import one.show.live.live.po.POGiftList;

public abstract class GetGiftsListRequest extends BaseBizRequest<POGiftList> {
    @Override
    public String getPath() {
        return BaseAPI.Path.gift_list;
    }

    @Override
    public void onRequestResult(String result) {
        System.out.println(result);
        Type type = new TypeToken<POCommonResp<POGiftList>>() {
        }.getType();

        responseBean = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.PUBLIC)
                .create().fromJson(result, type);
    }

}

