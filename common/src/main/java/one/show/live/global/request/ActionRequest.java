package one.show.live.global.request;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import butterknife.internal.Utils;
import one.show.live.api.CommonRequest;
import one.show.live.po.POCommonResp;
import one.show.live.po.POLaunch;
import one.show.live.common.BuildConfig;

/**
 * 打点统计的request 16/9/1.
 */
public abstract class ActionRequest extends CommonRequest<Object> {

    /**
     * JSON解析
     *
     * @param result
     */
    @Override
    public void onRequestResult(String result) {
        Type type = new TypeToken<POCommonResp<Object>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }

    @Override
    public String getRequestUrl() {
        return POLaunch.url+getUrl();
    }
    protected abstract String  getUrl();
}
