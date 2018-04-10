package one.show.live.netutil.netmanager.okhttp;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import one.show.live.netutil.netmanager.common.BaseNetResponse;
import one.show.live.netutil.netmanager.common.NetResponseListener;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class ObjectCallbackWrapper extends AbsCallbackWrapper<Object, Integer, NetResponseListener> {

    private Class mClssOfType;


    public ObjectCallbackWrapper(NetResponseListener listener, Integer flag, Class cls) {
        super(listener, flag);
        this.mClssOfType = cls;
    }

    @Override
    public Object parseNetworkResponse(Response response) throws Exception {
        if (mClssOfType == null) {
            return response.body().string();
        }
        if (JSONObject.class.isAssignableFrom(mClssOfType)) {
            return new JSONObject(response.body().string());
        }

        return jsonToolGetObject(response.body().string(), mClssOfType);
    }

    @Override
    public void onResponse(boolean isFromCache, Object o, Request request, @Nullable Response response) {
        NetResponseListener listener = getListener();
        if (listener != null) {
            BaseNetResponse netResponse = new OkhttpNetResponse()
                    .setFlag(getFlag())
                    .setFromCache(isFromCache)
                    .setExtra(o)
                    .setResponse(response);
            listener.onNetResponse(netResponse);
        }
    }

    @Override
    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onError(isFromCache, call, response, e);
        NetResponseListener listener = getListener();
        if (listener != null) {
            BaseNetResponse netResponse = new OkhttpNetResponse()
                    .setFlag(getFlag())
                    .setFromCache(isFromCache)
                    .setResponse(response)
                    .setException(e);
            listener.onNetError(netResponse);
        }
    }

    @Override
    public void onAfter(boolean isFromCache, @Nullable Object o, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onAfter(isFromCache, o, call, response, e);
    }

    private Object jsonToolGetObject(String jsonStr, Class clazz) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            if (jsonStr != null) {
                Object pk = gson.fromJson(jsonStr, clazz);
                return pk;
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
