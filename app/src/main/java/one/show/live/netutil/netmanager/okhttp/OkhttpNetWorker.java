package one.show.live.netutil.netmanager.okhttp;

import android.text.TextUtils;

import java.io.File;
import java.util.Map;

import one.show.live.R;
import one.show.live.netutil.netmanager.common.BaseNetWorker;
import one.show.live.netutil.netmanager.common.NetExecutor;
import one.show.live.netutil.netmanager.common.NetLoadingListener;
import one.show.live.netutil.netmanager.common.NetMethod;
import one.show.live.netutil.netmanager.common.NetResponseListener;
import one.show.live.netutil.netmanager.common.NetStatusCode;
import one.show.live.netutil.netmanager.utils.NetUtils;
import one.show.live.netutil.okhttputils.OkHttpUtils;
import one.show.live.netutil.okhttputils.callback.AbsCallback;
import one.show.live.netutil.okhttputils.model.HttpHeaders;
import one.show.live.netutil.okhttputils.model.HttpParams;
import one.show.live.netutil.okhttputils.request.BaseRequest;
import one.show.live.netutil.okhttputils.request.DeleteRequest;
import one.show.live.netutil.okhttputils.request.GetRequest;
import one.show.live.netutil.okhttputils.request.PostRequest;


public class OkhttpNetWorker extends BaseNetWorker {

    @Override
    public void cancel(Object tag) {
        if (tag != null) {
            OkHttpUtils.getInstance().cancelTag(tag);
        }
    }

    @Override
    public void requestDelete(String url, Map<String, String> headers, String body, Integer flag, Object tag, Class cls, NetResponseListener listener) {
        if (!checkNetwork(flag, listener)) {
            return;
        }
        HttpHeaders httpHeaders = getHttpHeaders(headers);
        AbsCallback callback = new ObjectCallbackWrapper(listener, flag, cls);
        DeleteRequest request = OkHttpUtils.delete(url);
        request.content(body);
        if (httpHeaders != null) {
            request.headers(httpHeaders);
        }
        if (tag != null) {
            request.tag(tag);
        }
        request.execute(callback);
    }

    @Override
    public void formRequest(int method, String url, Map<String, String> headers, Map<String, String> params,
                            Integer flag, Object tag, Class cls, NetResponseListener listener) {

        if (!checkNetwork(flag, listener)) {
            return;
        }
        HttpHeaders httpHeaders = getHttpHeaders(headers);
        HttpParams httpParams = getHttpParams(params);
        AbsCallback callback = new ObjectCallbackWrapper(listener, flag, cls);
        BaseRequest request = getRequestByMethod(method, url);
        if (httpHeaders != null) {
            request.headers(httpHeaders);
        }
        if (httpParams != null) {
            request.params(httpParams);
        }
        if (tag != null) {
            request.tag(tag);
        }

        request.execute(callback);
    }

    @Override
    public void jsonRequestPost(String url, Map<String, String> headers, String json,
                                Integer flag, Object tag, Class cls, NetResponseListener listener) {
        if (!checkNetwork(flag, listener)) {
            return;
        }
        HttpHeaders httpHeaders = getHttpHeaders(headers);
        AbsCallback callback = new ObjectCallbackWrapper(listener, flag, cls);
        PostRequest request = OkHttpUtils.post(url);
        if (httpHeaders != null) {
            request.headers(httpHeaders);
        }
        if (json != null) {
            request.postJson(json);
        }
        if (tag != null) {
            request.tag(tag);
        }

        request.execute(callback);
    }

    @Override
    public void downloadFile(String url, Map<String, String> headers, Map<String, String> params,
                             Integer flag, Object tag, NetLoadingListener listener, String destFileDir, String destFileName) {
        HttpHeaders httpHeaders = getHttpHeaders(headers);
        HttpParams httpParams = getHttpParams(params);
        AbsCallback callback = new FileCallbackWrapper(destFileDir, destFileName, listener, flag);
        GetRequest request = OkHttpUtils.get(url);
        if (httpHeaders != null) {
            request.headers(httpHeaders);
        }
        if (httpParams != null) {
            request.params(httpParams);
        }
        if (tag != null) {
            request.tag(tag);
        }
        request.execute(callback);
    }

    @Override
    public void uploadFile(String url, Map<String, String> headers, Map<String, String> params, Map<String, String> files,
                           Integer flag, Object tag, NetLoadingListener listener) {
        HttpHeaders httpHeaders = getHttpHeaders(headers);
        HttpParams httpParams = getHttpParams(params);
        HttpParams fileParams = getHttpFileParams(files);
        PostRequest request = OkHttpUtils.post(url);
        AbsCallback callback = new JsonCallbackWrapper(listener, flag);
        if (httpHeaders != null) {
            request.headers(httpHeaders);
        }
        if (httpParams != null) {
            request.params(httpParams);
        }
        if (fileParams != null) {
            request.params(fileParams);
        }
        if (tag != null) {
            request.tag(tag);
        }
        request.execute(callback);
    }

    private BaseRequest getRequestByMethod(int method, String url) {
        BaseRequest request;
        if (method == NetMethod.GET) {
            request = OkHttpUtils.get(url);
        } else if (method == NetMethod.POST) {
            request = OkHttpUtils.post(url);
        } else if (method == NetMethod.PUT) {
            request = OkHttpUtils.put(url);
        } else if (method == NetMethod.DELETE) {
            request = OkHttpUtils.delete(url);
        } else if (method == NetMethod.HEAD) {
            request = OkHttpUtils.head(url);
        } else if (method == NetMethod.OPTIONS) {
            request = OkHttpUtils.options(url);
        }
        //else if (method == NetMethod.TRACE) { }
        //else if (method == NetMethod.PATCH) { }
        else {
            request = OkHttpUtils.post(url);
        }

        return request;
    }

    private HttpHeaders getHttpHeaders(Map<String, String> httpHeaderMap) {
        if (httpHeaderMap != null && httpHeaderMap.size() > 0) {
            HttpHeaders httpHeaders = new HttpHeaders();
            for (String key : httpHeaderMap.keySet()) {
                String value = httpHeaderMap.get(key);
                if (value != null) {
                    String[] headers = value.split(NetExecutor.HEADER_SEPARATE);
                    for (String header : headers) {
                        httpHeaders.put(key, header);
                    }
                }
            }
            return httpHeaders;
        }
        return null;
    }

    private HttpParams getHttpParams(Map<String, String> httpParamsMap) {
        if (httpParamsMap != null && httpParamsMap.size() > 0) {
            HttpParams httpParams = new HttpParams();
            for (String key : httpParamsMap.keySet()) {
                String value = httpParamsMap.get(key);
                if (value != null) {
                    String[] params = value.split(NetExecutor.PARAM_SEPARATE);
                    for (String param : params) {
                        httpParams.put(key, param);
                    }
                }
            }
            return httpParams;
        }
        return null;
    }

    private HttpParams getHttpFileParams(Map<String, String> httpParamsMap) {
        if (httpParamsMap != null && httpParamsMap.size() > 0) {
            HttpParams httpParams = new HttpParams();
            for (String key : httpParamsMap.keySet()) {
                String value = httpParamsMap.get(key);
                if (value != null) {
                    String[] params = value.split(NetExecutor.PARAM_SEPARATE);
                    for (String param : params) {
                        if (!TextUtils.isEmpty(param)) {
                            httpParams.put(key, new File(param));
                        }
                    }
                }
            }
            return httpParams;
        }
        return null;
    }

    private boolean checkNetwork(final int flag, final NetResponseListener listener) {
        if (!NetUtils.isNetworkConnected(OkHttpUtils.getContext())) {
            if (listener != null) {
                OkHttpUtils.getInstance().getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onNetError(
                                new OkhttpNetResponse()
                                        .setFlag(flag)
                                        .setMsg(OkHttpUtils.getContext().getString(R.string.network_state_closed))
                                        .setState(NetStatusCode.UNKNOWN_ERROR)
                                        .setException(new IllegalStateException(
                                                OkHttpUtils.getContext().getString(R.string.network_state_closed)))

                        );
                    }
                });
            }
            return false;
        }
        return true;
    }

}
