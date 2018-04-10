package one.show.live.netutil.netmanager.okhttp;

import android.support.annotation.Nullable;

import org.json.JSONObject;

import one.show.live.netutil.netmanager.common.BaseNetResponse;
import one.show.live.netutil.netmanager.common.NetLoadingListener;
import one.show.live.netutil.okhttputils.request.BaseRequest;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


public class JsonCallbackWrapper extends AbsCallbackWrapper<JSONObject, Integer, NetLoadingListener> {

    public JsonCallbackWrapper(NetLoadingListener listener, Integer flag) {
        super(listener, flag);
    }

    @Override
    public JSONObject parseNetworkResponse(Response response) throws Exception {
        return new JSONObject(response.body().string());
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        NetLoadingListener listener = getListener();
        if (listener != null) {
            listener.onBefore();
        }
    }

    @Override
    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        super.upProgress(currentSize, totalSize, progress, networkSpeed);
        NetLoadingListener listener = getListener();
        if (listener != null) {
            listener.onProgress(
                    new OkhttpNetLoading().setFlag(getFlag())
                            .setCurrentSize(currentSize)
                            .setTotalSize(totalSize)
                            .setProgress(progress)
                            .setNetworkSpeed(networkSpeed)
            );
        }
    }

    @Override
    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
        NetLoadingListener listener = getListener();
        if (listener != null) {
            listener.onProgress(
                    new OkhttpNetLoading().setFlag(getFlag())
                            .setCurrentSize(currentSize)
                            .setTotalSize(totalSize)
                            .setProgress(progress)
                            .setNetworkSpeed(networkSpeed)
            );
        }
    }

    @Override
    public void onResponse(boolean isFromCache, JSONObject jsonObject, Request request, @Nullable Response response) {
        NetLoadingListener listener = getListener();
        if (listener != null) {
            BaseNetResponse netResponse = new OkhttpNetResponse()
                    .setFlag(getFlag())
                    .setExtra(jsonObject)
                    .setFromCache(isFromCache)
                    .setResponse(response);

            listener.onNetResponse(netResponse);
        }
    }

    @Override
    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onError(isFromCache, call, response, e);
        NetLoadingListener listener = getListener();
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
    public void onAfter(boolean isFromCache, @Nullable JSONObject jsonObject, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onAfter(isFromCache, jsonObject, call, response, e);
        NetLoadingListener listener = getListener();
        if (listener != null) {
            BaseNetResponse netResponse = new OkhttpNetResponse()
                    .setFlag(getFlag())
                    .setExtra(jsonObject)
                    .setFromCache(isFromCache)
                    .setResponse(response)
                    .setException(e);

            listener.onAfter(netResponse);
        }
    }

}
