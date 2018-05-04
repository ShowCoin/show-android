package one.show.live.showlive.im;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.common.po.POCommonResp;
import one.show.live.po.PORyToken;

/**
 * Request to update IM token.
 */
public abstract class UpdateRyTokenRequest extends BaseBizRequest<PORyToken> {
    @Override
    public String getPath() {
        return BaseAPI.Path.rongToken;
    }

    @Override
    public void onRequestResult(String result) {
        Type type = new TypeToken<POCommonResp<PORyToken>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }

}
