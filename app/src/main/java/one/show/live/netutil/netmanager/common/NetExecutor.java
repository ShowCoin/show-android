package one.show.live.netutil.netmanager.common;

import org.json.JSONObject;

import java.util.Map;

import one.show.live.netutil.netmanager.okhttp.OkhttpNetWorker;
import one.show.live.netutil.netmanager.schedulers.Schedulers;
import one.show.live.netutil.okhttputils.OkHttpUtils;


public class NetExecutor {

    public static final String HEADER_SEPARATE = "&";

    public static final String PARAM_SEPARATE = "&";

    private BaseNetWorker mNetworker;

    private NetCache mNetCache;

    private static NetExecutor mInstance;

    private NetExecutor() {
        this.mNetworker = new OkhttpNetWorker();
        this.mNetCache = new NetCache();
    }

    public synchronized static NetExecutor getInstance() {
        if (mInstance == null) {
            mInstance = new NetExecutor();
        }
        return mInstance;
    }


    private Map<String, String> getHeadParams(Map<String, String> params) {
        return OkHttpUtils.getInstance().getIsSign() ? OkHttpUtils.getInstance().getHeadParams(params) : null;
    }

    /**
     * @param method   GET，POST请求 NetMethod
     * @param url      地址
     * @param params   请求参数
     * @param flag     标志 （同一个网络请求，同一个listener回调的时候通过flag 区分）
     * @param tag      取消请求时会用到tag
     * @param cls      请求实体类
     * @param listener 回调
     */
    public void formRequest(int method, String url, Map<String, String> params,
                            int flag, Object tag, Class cls, NetResponseListener listener) {
        formRequest(method, url, getHeadParams(params), params, flag, tag, cls, listener);
    }

    /**
     * 下载
     *
     * @param url
     * @param params
     * @param flag
     * @param tag
     * @param listener
     * @param destFileDir
     * @param destFileName
     */
    public void downloadFile(String url, Map<String, String> params,
                             int flag, Object tag, NetLoadingListener listener, String destFileDir, String destFileName) {
        downloadFile(url, null, params, flag, tag, listener, destFileDir, destFileName);
    }

    /**
     * 上传文件
     *
     * @param url
     * @param params
     * @param files
     * @param flag
     * @param tag
     * @param listener
     */
    public void uploadFile(String url, Map<String, String> params, Map<String, String> files,
                           int flag, Object tag, NetLoadingListener listener) {
        uploadFile(url, getHeadParams(params), params, files, flag, tag, listener);
    }

    private void formRequest(int method, String url, Map<String, String> headers, Map<String, String> params,
                             int flag, Object tag, Class cls, NetResponseListener listener) {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setMethod(method)
                .setUrl(url)
                .setHeaders(headers)
                .setParams(params)
                .setFlag(flag)
                .setTag(tag)
                .setCls(cls)
                .setListener(listener);
        this.mNetCache.addRequest(dataRequest);
        final NetResponseListener listenerWrapper = new RootResponseListener(this.mNetCache, dataRequest);
        this.mNetworker.formRequest(method, url, headers, params, flag, tag, cls, listenerWrapper);
    }

    private void requestDelete(String url, Map<String, String> headers, String body,
                               int flag, Object tag, Class cls, NetResponseListener listener) {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setMethod(NetMethod.DELETE)
                .setUrl(url)
                .setHeaders(headers)
                .setBody(body)
                .setFlag(flag)
                .setTag(tag)
                .setCls(cls)
                .setListener(listener);
        this.mNetCache.addRequest(dataRequest);
        final NetResponseListener listenerWrapper = new RootResponseListener(this.mNetCache, dataRequest);
        this.mNetworker.requestDelete(url, headers, body, flag, tag, cls, listenerWrapper);
    }


    private void jsonRequestPost(String url, Map<String, String> headers, Map<String, String> params,
                                 int flag, Object tag, Class cls, NetResponseListener listener) {
        String body = "";
        try {
            JSONObject json = new JSONObject(params);
            body = json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.jsonRequestPost(url, headers, body, flag, tag, cls, listener);
    }

    public void jsonRequestPost(String url, Map<String, String> headers, JSONObject json,
                                int flag, Object tag, Class cls, NetResponseListener listener) {
        this.jsonRequestPost(url, headers, json == null ? "" : json.toString(), flag, tag, cls, listener);
    }

    public void jsonRequestPost(String url, Map<String, String> headers, String body,
                                int flag, Object tag, Class cls, NetResponseListener listener) {
        DataRequest dataRequest = new DataRequest();
        dataRequest.setUrl(url)
                .setHeaders(headers)
                .setBody(body)
                .setFlag(flag)
                .setTag(tag)
                .setCls(cls)
                .setListener(listener);
        this.mNetCache.addRequest(dataRequest);
        final NetResponseListener listenerWrapper = new RootResponseListener(this.mNetCache, dataRequest);
        this.mNetworker.jsonRequestPost(url, headers, body == null ? "" : body, flag, tag, cls, listenerWrapper);
    }

    private void downloadFile(String url, Map<String, String> headers, Map<String, String> params,
                              int flag, Object tag, NetLoadingListener listener, String destFileDir, String destFileName) {
        LoadingRequest loadingRequest = new LoadingRequest();
        loadingRequest.setUrl(url)
                .setHeaders(headers)
                .setParams(params)
                .setFlag(flag)
                .setTag(tag)
                .setListener(listener)
                .setDestFileDir(destFileDir)
                .setDestFileName(destFileName);
        this.mNetCache.addRequest(loadingRequest);
        final NetLoadingListener listenerWrapper = new RootLoadingListener(this.mNetCache, loadingRequest);
        this.mNetworker.downloadFile(url, headers, params, flag, tag, listenerWrapper, destFileDir, destFileName);
    }

    private void uploadFile(String url, Map<String, String> headers, Map<String, String> params, Map<String, String> files,
                            int flag, Object tag, NetLoadingListener listener) {
        LoadingRequest loadingRequest = new LoadingRequest();
        loadingRequest.setUrl(url)
                .setHeaders(headers)
                .setParams(params)
                .setFlag(flag)
                .setTag(tag)
                .setListener(listener)
                .setFiles(files);
        this.mNetCache.addRequest(loadingRequest);
        final NetLoadingListener listenerWrapper = new RootLoadingListener(this.mNetCache, loadingRequest);
        this.mNetworker.uploadFile(url, headers, params, files, flag, tag, listenerWrapper);
    }

    /**
     * 回调的取消是同步的
     * 网络请求的取消是异步（同步的话会阻塞当前线程）
     * 因此建议调用该方法之后，重新new一个tag，否则有可能会将之后的请求一并取消！！！
     */
    public void cancel(final Object tag) {
        this.mNetCache.cancelRequests(tag);
        Schedulers.getInstance().background(new Runnable() {
            @Override
            public void run() {
                mNetworker.cancel(tag);
            }
        });
    }

}
