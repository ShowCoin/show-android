package one.show.live.upload.biz.avatarupload;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseRequestSync;
import one.show.live.common.po.POCommonResp;
import one.show.live.po.PONew;


public class UpdateUserInfoFormRequest extends BaseRequestSync<PONew> {
    @Override
    public String getPath() {
        return BaseAPI.Path.updateUserInfo;
    }

    @Override
    public void onRequestResult(String result) {
        Type type = new TypeToken<POCommonResp<PONew>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }
}